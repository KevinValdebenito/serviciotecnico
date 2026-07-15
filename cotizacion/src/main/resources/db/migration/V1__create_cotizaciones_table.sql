CREATE TABLE cotizaciones (
    id UUID PRIMARY KEY,
    ticket_id UUID NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    mano_obra NUMERIC(12, 2) NOT NULL,
    costo_repuestos NUMERIC(12, 2) NOT NULL,
    descuento NUMERIC(12, 2) NOT NULL DEFAULT 0,
    subtotal NUMERIC(12, 2) NOT NULL,
    neto NUMERIC(12, 2) NOT NULL,
    iva NUMERIC(12, 2) NOT NULL,
    total NUMERIC(12, 2) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP
);

CREATE TABLE cotizacion_detalles (
    id UUID PRIMARY KEY,
    cotizacion_id UUID NOT NULL,
    repuesto_id UUID NOT NULL,
    nombre_repuesto VARCHAR(150) NOT NULL,
    precio_unitario NUMERIC(12, 2) NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    subtotal NUMERIC(12, 2) NOT NULL,
    CONSTRAINT fk_cotizacion_detalle
        FOREIGN KEY (cotizacion_id)
        REFERENCES cotizaciones(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_cotizaciones_ticket_id
    ON cotizaciones(ticket_id);

CREATE INDEX idx_cotizacion_detalles_cotizacion_id
    ON cotizacion_detalles(cotizacion_id);
