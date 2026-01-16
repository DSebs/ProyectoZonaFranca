-- Crear tabla de citas
CREATE TABLE citas (
    id VARCHAR(36) PRIMARY KEY,
    tipo_cita VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    estado_post_cita VARCHAR(20),
    
    -- Información del proveedor
    nombre_proveedor VARCHAR(200) NOT NULL,
    nit VARCHAR(50) NOT NULL,
    numero_orden_compra VARCHAR(100) NOT NULL,
    
    -- Datos del responsable
    responsable_nombre VARCHAR(100) NOT NULL,
    responsable_email VARCHAR(100) NOT NULL,
    responsable_telefono VARCHAR(20) NOT NULL,
    
    -- Información de transporte
    tipo_transporte VARCHAR(20) NOT NULL,
    
    -- Campos para transportadora
    nombre_transportadora VARCHAR(200),
    numero_guia VARCHAR(100),
    
    -- Campos para transporte particular
    conductor_nombre VARCHAR(100),
    conductor_cedula VARCHAR(20),
    placa_vehiculo VARCHAR(10),
    
    -- Auxiliar de transporte (opcional)
    auxiliar_nombre VARCHAR(100),
    auxiliar_cedula VARCHAR(20),
    
    -- Horario
    fecha_hora TIMESTAMP NOT NULL,
    
    -- Observaciones
    observaciones TEXT,
    
    -- Auditoría
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_ultima_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_tipo_cita CHECK (tipo_cita IN ('ENTREGA', 'RECOJO', 'IMPORTACION', 'DEVOLUCION')),
    CONSTRAINT chk_estado CHECK (estado IN ('PENDIENTE', 'CONFIRMADA', 'RECHAZADA', 'CANCELADA')),
    CONSTRAINT chk_estado_post_cita CHECK (estado_post_cita IN ('ENTREGADO', 'DEVUELTO', 'TARDIA')),
    CONSTRAINT chk_tipo_transporte CHECK (tipo_transporte IN ('TRANSPORTADORA', 'PARTICULAR')),
    
    -- Validaciones condicionales
    CONSTRAINT chk_transportadora_data CHECK (
        (tipo_transporte = 'TRANSPORTADORA' AND nombre_transportadora IS NOT NULL AND numero_guia IS NOT NULL) OR
        (tipo_transporte = 'PARTICULAR' AND conductor_nombre IS NOT NULL AND conductor_cedula IS NOT NULL AND placa_vehiculo IS NOT NULL)
    ),
    
    -- El estado post-cita solo puede existir si el estado es CONFIRMADA
    CONSTRAINT chk_estado_post_cita_valido CHECK (
        (estado_post_cita IS NULL) OR 
        (estado_post_cita IS NOT NULL AND estado = 'CONFIRMADA')
    )
);

-- Índices para optimizar consultas frecuentes
CREATE INDEX idx_cita_estado ON citas(estado);
CREATE INDEX idx_cita_tipo ON citas(tipo_cita);
CREATE INDEX idx_cita_nit ON citas(nit);
CREATE INDEX idx_cita_fecha_hora ON citas(fecha_hora);
CREATE INDEX idx_cita_tipo_estado ON citas(tipo_cita, estado);
CREATE INDEX idx_cita_fecha_creacion ON citas(fecha_creacion);

-- Índice compuesto para búsquedas de conflictos
CREATE INDEX idx_cita_conflicto ON citas(tipo_cita, fecha_hora, estado) 
WHERE estado IN ('PENDIENTE', 'CONFIRMADA');

-- Índice para búsquedas por proveedor
CREATE INDEX idx_cita_proveedor ON citas(nit, fecha_creacion DESC);

-- Comentarios en la tabla
COMMENT ON TABLE citas IS 'Tabla principal para almacenar las citas de proveedores';
COMMENT ON COLUMN citas.id IS 'Identificador único de la cita (UUID)';
COMMENT ON COLUMN citas.tipo_cita IS 'Tipo de cita: ENTREGA, RECOJO, IMPORTACION, DEVOLUCION';
COMMENT ON COLUMN citas.estado IS 'Estado actual de la cita';
COMMENT ON COLUMN citas.estado_post_cita IS 'Estado posterior a la ejecución de la cita (solo para confirmadas)';
COMMENT ON COLUMN citas.fecha_hora IS 'Fecha y hora programada para la cita';
COMMENT ON COLUMN citas.tipo_transporte IS 'Tipo de transporte: TRANSPORTADORA o PARTICULAR';