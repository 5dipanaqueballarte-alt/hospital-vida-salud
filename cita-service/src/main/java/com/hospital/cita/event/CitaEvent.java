package com.hospital.cita.event;

import java.time.LocalDateTime;

public record CitaEvent(
        Long citaId,
        Long pacienteId,
        LocalDateTime fechaHora,
        String estado,
        LocalDateTime timestamp) {}