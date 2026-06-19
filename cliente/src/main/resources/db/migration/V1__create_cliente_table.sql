CREATE TABLE IF NOT EXISTS clientes (
    email VARCHAR(255) PRIMARY KEY,
    rut VARCHAR(20) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    telefono VARCHAR(50),
    direccion VARCHAR(255)
);