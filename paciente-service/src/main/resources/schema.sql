CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(200),
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT uk_paciente_documento UNIQUE (tipo_documento, numero_documento)
    );