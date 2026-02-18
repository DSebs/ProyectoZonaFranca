-- Migración V2: Crear tablas de usuarios y auditoría

-- ============================================
-- TABLA: usuarios
-- ============================================
CREATE TABLE usuarios (
    id VARCHAR(36) PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL,
    correo_usuario VARCHAR(100) NOT NULL UNIQUE,
    contraseña_usuario VARCHAR(255) NOT NULL,
    estado_cuenta BOOLEAN NOT NULL DEFAULT TRUE,
    tipo_cuenta VARCHAR(20) NOT NULL,
    
    -- Constraints
    CONSTRAINT chk_tipo_cuenta CHECK (tipo_cuenta IN ('ADMINISTRADOR', 'VISUALIZADOR'))
);

-- Índices para usuarios
CREATE INDEX idx_usuario_correo ON usuarios(correo_usuario);
CREATE INDEX idx_usuario_tipo ON usuarios(tipo_cuenta);
CREATE INDEX idx_usuario_activo ON usuarios(estado_cuenta);

-- Comentarios
COMMENT ON TABLE usuarios IS 'Tabla de usuarios del sistema (empleados y administradores)';
COMMENT ON COLUMN usuarios.id IS 'Identificador único del usuario (UUID)';
COMMENT ON COLUMN usuarios.nombre_usuario IS 'Nombre completo del usuario';
COMMENT ON COLUMN usuarios.correo_usuario IS 'Correo electrónico único para autenticación';
COMMENT ON COLUMN usuarios.contraseña_usuario IS 'Contraseña hasheada con BCrypt';
COMMENT ON COLUMN usuarios.estado_cuenta IS 'Indica si la cuenta está activa';
COMMENT ON COLUMN usuarios.tipo_cuenta IS 'Tipo de usuario: ADMINISTRADOR o VISUALIZADOR';

-- ============================================
-- TABLA: auditoria_cambios_estado
-- ============================================
CREATE TABLE auditoria_cambios_estado (
    id VARCHAR(36) PRIMARY KEY,
    cita_id VARCHAR(36) NOT NULL,
    usuario_id VARCHAR(36) NOT NULL,
    usuario_nombre VARCHAR(100) NOT NULL,
    tipo_cambio VARCHAR(30) NOT NULL,
    estado_anterior VARCHAR(20),
    estado_nuevo VARCHAR(20) NOT NULL,
    observaciones TEXT,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign Keys
    CONSTRAINT fk_auditoria_cita FOREIGN KEY (cita_id) 
        REFERENCES citas(id) ON DELETE CASCADE,
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE SET NULL,
    
    -- Constraints
    CONSTRAINT chk_tipo_cambio CHECK (tipo_cambio IN (
        'CONFIRMACION', 
        'RECHAZO', 
        'CANCELACION', 
        'ASIGNACION_ENTREGADO', 
        'ASIGNACION_DEVUELTO', 
        'ASIGNACION_TARDIA'
    ))
);

-- Índices para auditoría
CREATE INDEX idx_auditoria_cita_id ON auditoria_cambios_estado(cita_id);
CREATE INDEX idx_auditoria_usuario_id ON auditoria_cambios_estado(usuario_id);
CREATE INDEX idx_auditoria_tipo_cambio ON auditoria_cambios_estado(tipo_cambio);
CREATE INDEX idx_auditoria_fecha_cambio ON auditoria_cambios_estado(fecha_cambio DESC);

-- Índice compuesto para consultas frecuentes
CREATE INDEX idx_auditoria_usuario_fecha ON auditoria_cambios_estado(usuario_id, fecha_cambio DESC);

-- Comentarios
COMMENT ON TABLE auditoria_cambios_estado IS 'Registro de auditoría para cambios de estado en citas';
COMMENT ON COLUMN auditoria_cambios_estado.id IS 'Identificador único del registro de auditoría (UUID)';
COMMENT ON COLUMN auditoria_cambios_estado.cita_id IS 'Referencia a la cita modificada';
COMMENT ON COLUMN auditoria_cambios_estado.usuario_id IS 'Referencia al usuario que realizó el cambio';
COMMENT ON COLUMN auditoria_cambios_estado.usuario_nombre IS 'Nombre del usuario al momento del cambio (desnormalizado)';
COMMENT ON COLUMN auditoria_cambios_estado.tipo_cambio IS 'Tipo de cambio realizado';
COMMENT ON COLUMN auditoria_cambios_estado.estado_anterior IS 'Estado previo al cambio (NULL para post-citas)';
COMMENT ON COLUMN auditoria_cambios_estado.estado_nuevo IS 'Nuevo estado después del cambio';
COMMENT ON COLUMN auditoria_cambios_estado.observaciones IS 'Observaciones o motivo del cambio';
COMMENT ON COLUMN auditoria_cambios_estado.fecha_cambio IS 'Fecha y hora del cambio';
