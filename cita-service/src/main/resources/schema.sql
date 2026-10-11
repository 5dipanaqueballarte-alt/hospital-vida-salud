CREATE TABLE IF NOT EXISTS citas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    especialidad VARCHAR(80) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    profesional_codigo VARCHAR(30) NOT NULL,
    motivo VARCHAR(200),
    estado VARCHAR(15) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE UNIQUE INDEX IF NOT EXISTS uk_cita_agenda
    ON citas (profesional_codigo, fecha_hora)
    WHERE estado <> 'CANCELADA';