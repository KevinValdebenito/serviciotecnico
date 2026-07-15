CREATE TABLE IF NOT EXISTS diagnosticos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticket_id UUID NOT NULL,
    empleado_id UUID NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    fecha TIMESTAMP NOT NULL
);