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

CREATE INDEX idx_cotizaciones_ticket_id
ON cotizaciones(ticket_id);  