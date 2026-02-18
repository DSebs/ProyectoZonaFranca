# Modelo de Datos y Arquitectura del Sistema

## Diagrama Entidad-Relación

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           MODELO DE DATOS                                    │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────┐         ┌─────────────────────────────┐
│         USUARIOS            │         │           CITAS             │
├─────────────────────────────┤         ├─────────────────────────────┤
│ PK id VARCHAR(36)           │         │ PK id VARCHAR(36)           │
│    nombre_usuario           │         │    tipo_cita                │
│    correo_usuario (UNIQUE)  │         │    estado                   │
│    contraseña_usuario       │         │    estado_post_cita         │
│    estado_cuenta            │         │    nombre_proveedor         │
│    tipo_cuenta              │         │    nit                      │
│    (ADMINISTRADOR |         │         │    numero_orden_compra      │
│     VISUALIZADOR)           │         │    responsable_nombre       │
└──────────────┬──────────────┘         │    responsable_email        │
               │                        │    responsable_telefono     │
               │                        │    tipo_transporte          │
               │ 1                      │    nombre_transportadora    │
               │                        │    numero_guia              │
               │                        │    conductor_nombre         │
               │                        │    conductor_cedula         │
               │                        │    placa_vehiculo           │
               │                        │    auxiliar_nombre          │
               │                        │    auxiliar_cedula          │
               ▼                        │    fecha_hora               │
┌─────────────────────────────┐         │    observaciones            │
│   AUDITORIA_CAMBIOS_ESTADO  │         │    fecha_creacion           │
├─────────────────────────────┤         │    fecha_ultima_modificacion│
│ PK id VARCHAR(36)           │         └──────────────┬──────────────┘
│ FK cita_id ─────────────────┼─────────────────────────┘
│ FK usuario_id ──────────────┼── (ref USUARIOS.id, ON DELETE SET NULL)
│    usuario_nombre           │
│    tipo_cambio              │
│    estado_anterior          │
│    estado_nuevo             │
│    observaciones            │
│    fecha_cambio             │
└─────────────────────────────┘
```

## Relaciones

| Tabla Origen | Tabla Destino | Tipo | Descripción |
|--------------|---------------|------|-------------|
| `auditoria_cambios_estado` | `citas` | N:1 | Una cita puede tener múltiples registros de auditoría |
| `auditoria_cambios_estado` | `usuarios` | N:1 | Un usuario puede realizar múltiples cambios |

## Flujo del Sistema

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                        FLUJO DE UNA CITA                                      │
└──────────────────────────────────────────────────────────────────────────────┘

     PROVEEDOR                   SISTEMA                    EMPLEADO/ADMIN
         │                          │                             │
         │  1. Solicita cita        │                             │
         │  (formulario público)    │                             │
         │─────────────────────────>│                             │
         │                          │                             │
         │                     ┌────┴────┐                        │
         │                     │ PENDIENTE│◄─────────────────────┐ │
         │                     └────┬────┘                       │ │
         │                          │                            │ │
         │                          │ 2. Revisa y decide         │ │
         │                          │◄───────────────────────────┼─┤
         │                          │                            │ │
         │              ┌───────────┼───────────┐                │ │
         │              │           │           │                │ │
         │              ▼           ▼           ▼                │ │
         │       ┌──────────┐ ┌──────────┐ ┌──────────┐          │ │
         │       │CONFIRMADA│ │RECHAZADA │ │CANCELADA │          │ │
         │       └────┬─────┘ └──────────┘ └──────────┘          │ │
         │            │                                          │ │
         │  3. Recibe │ 4. Asigna estado post-cita               │ │
         │  email     │◄─────────────────────────────────────────┼─┤
         │◄───────────│                                          │ │
         │            ▼                                          │ │
         │    ┌───────────────────┐                              │ │
         │    │ ENTREGADO |       │                              │ │
         │    │ DEVUELTO  | TARDIA│                              │ │
         │    └───────────────────┘                              │ │
         │                                                       │ │
         │            ┌────────────────────────────────────────┐ │ │
         │            │        AUDITORÍA (cada cambio)          │ │ │
         │            │  - QUIÉN: Usuario que realizó el cambio │ │ │
         │            │  - QUÉ: Cita modificada                  │ │
         │            │  - CUÁNDO: Fecha/hora del cambio        │◄┘ │
         │            │  - TIPO: Confirmación/Rechazo/Cancel... │   │
         │            └────────────────────────────────────────┘   │
         │                                                         │
         └─────────────────────────────────────────────────────────┘
```

## Arquitectura Hexagonal

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CAPA EXTERNA (API)                                  │
│  ┌────────────────────┐ ┌────────────────────┐ ┌────────────────────┐       │
│  │  CitaController    │ │ UsuarioController  │ │AuditoriaController │       │
│  │  CitaEstadoContrl  │ │                    │ │                    │       │
│  │  HorarioController │ │                    │ │                    │       │
│  └─────────┬──────────┘ └─────────┬──────────┘ └─────────┬──────────┘       │
│            │                      │                      │                   │
│            │    DTOs: Request/Response                   │                   │
│            │    Mappers: CitaDtoMapper, etc.             │                   │
└────────────┼──────────────────────┼──────────────────────┼───────────────────┘
             │                      │                      │
             ▼                      ▼                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          PUERTOS (CASOS DE USO)                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                      DOMINIO CITAS                                   │    │
│  │  CrearCitaUseCase ─────────────────────────────────────────────┐    │    │
│  │  ConsultarCitasUseCase                                         │    │    │
│  │  GestionarEstadoCitaUseCase ──┬───────────────────────────────┐│    │    │
│  │                               │ Integra Auditoría y Notifica  ││    │    │
│  │  Servicios: ValidadorHorarios, GestorEstadosCita              ││    │    │
│  │  Puerto: CitaRepositorio                                       ││    │    │
│  └───────────────────────────────┼────────────────────────────────┼┘    │    │
│                                  │                                │     │    │
│  ┌───────────────────────────────▼────────────────────────────────▼┐    │    │
│  │                      DOMINIO AUDITORIA                          │    │    │
│  │  RegistrarCambioEstadoUseCase ◄───── GestionarEstadoCitaUseCase │    │    │
│  │  ConsultarAuditoriaUseCase                                      │    │    │
│  │  Puerto: AuditoriaRepositorio                                   │    │    │
│  └─────────────────────────────────────────────────────────────────┘    │    │
│                                                                          │    │
│  ┌─────────────────────────────────────────────────────────────────┐    │    │
│  │                      DOMINIO NOTIFICACION                        │    │    │
│  │  NotificarCambioEstadoCitaUseCase (@Async)                       │    │    │
│  │  Puerto: NotificacionService                                     │    │    │
│  └─────────────────────────────────────────────────────────────────┘    │    │
│                                                                          │    │
│  ┌─────────────────────────────────────────────────────────────────┐    │    │
│  │                      DOMINIO USUARIO                             │    │    │
│  │  CrearUsuarioUseCase                                             │    │    │
│  │  AutenticarUsuarioUseCase                                        │    │    │
│  │  ConsultarUsuariosUseCase                                        │    │    │
│  │  GestionarUsuarioUseCase                                         │    │    │
│  │  Servicios: AutenticadorUsuario, ValidadorUsuario                │    │    │
│  │  Puerto: UsuarioRepositorio, UsuarioActualService                │    │    │
│  └─────────────────────────────────────────────────────────────────┘    │    │
└──────────────────────────────────────────────────────────────────────────────┘
             │                      │                      │
             ▼                      ▼                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        ADAPTADORES (INFRAESTRUCTURA)                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                         PERSISTENCIA (JPA)                          │    │
│  │  CitaEntity ◄───► CitaMapper ◄───► CitaRepositorioImpl              │    │
│  │  UsuarioEntity ◄───► UsuarioMapper ◄───► UsuarioRepositorioImpl     │    │
│  │  RegistroAuditoriaEntity ◄───► AuditoriaMapper ◄───► AuditoriaRepoImpl│   │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                            EMAIL                                     │    │
│  │  EmailNotificacionServiceImpl (JavaMailSender + Thymeleaf)          │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                           SEGURIDAD                                  │    │
│  │  SecurityConfig, JwtAuthenticationFilter, UsuarioActualServiceImpl  │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          BASE DE DATOS (PostgreSQL)                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────┐              │
│  │   citas     │  │  usuarios   │  │ auditoria_cambios_estado│              │
│  └─────────────┘  └─────────────┘  └─────────────────────────┘              │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Dominios y sus Responsabilidades

### 1. Dominio CITAS (Núcleo del sistema)
- **Modelos**: `Cita`, `TipoCita`, `EstadoCita`, `EstadoPostCita`, `Horario`, `InformacionProveedor`, `DatosContacto`, `OpcionTransporte`
- **Responsabilidad**: Gestionar el ciclo de vida completo de las citas
- **Relaciones**: Orquesta los dominios de Auditoría y Notificación

### 2. Dominio USUARIO
- **Modelos**: `User`, `TipoUsuario`
- **Responsabilidad**: Autenticación, autorización y gestión de usuarios del sistema
- **Tipos**: `ADMINISTRADOR` (todos los permisos) y `VISUALIZADOR` (solo lectura y confirmación)

### 3. Dominio AUDITORÍA
- **Modelos**: `RegistroCambioEstado`, `TipoCambio`
- **Responsabilidad**: Registrar QUIÉN hizo CUÁL cambio y CUÁNDO en las citas
- **Tipos de cambio auditados**:
  - `CONFIRMACION` - Cita confirmada
  - `RECHAZO` - Cita rechazada
  - `CANCELACION` - Cita cancelada
  - `ASIGNACION_ENTREGADO` - Estado post-cita asignado
  - `ASIGNACION_DEVUELTO` - Estado post-cita asignado
  - `ASIGNACION_TARDIA` - Estado post-cita asignado

### 4. Dominio NOTIFICACIÓN
- **Modelos**: `NotificacionCita`, `TipoNotificacion`
- **Responsabilidad**: Enviar emails a los proveedores cuando cambia el estado de sus citas
- **Ejecución**: Asíncrona (`@Async`) para no bloquear el flujo principal

## Endpoints de la API

### Auditoría (`/api/auditoria`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/{id}` | Obtener registro por ID |
| GET | `/usuario/{usuarioId}` | **Filtro principal** - Registros por usuario |
| GET | `/usuario/{usuarioId}/resumen` | Resumen con conteo de cambios |
| GET | `/usuario/{usuarioId}/conteo` | Solo conteo de cambios |
| GET | `/cita/{citaId}` | Historial de cambios de una cita |
| GET | `/cita/{citaId}/conteo` | Conteo de cambios de una cita |
| GET | `/tipo/{tipoCambio}` | Filtrar por tipo de cambio |
| GET | `/rango?desde=X&hasta=Y` | Filtrar por rango de fechas |
| GET | `/ultimos?limite=N` | Últimos N registros |

## Integración de Auditoría

La auditoría se integra automáticamente en `GestionarEstadoCitaUseCase`:

```java
// Cuando un usuario cambia el estado de una cita:
1. Se obtiene el usuario actual del contexto de seguridad
2. Se registra el estado anterior
3. Se realiza el cambio en la cita
4. Se guarda en la BD
5. Se registra en auditoría (RegistrarCambioEstadoUseCase)
6. Se envía notificación al proveedor (asíncrono)
```

## Migraciones de Base de Datos

| Versión | Archivo | Descripción |
|---------|---------|-------------|
| V1 | `V1__Create_citas_table.sql` | Tabla `citas` con toda la información de la cita |
| V2 | `V2__Create_usuarios_and_auditoria_tables.sql` | Tablas `usuarios` y `auditoria_cambios_estado` |
