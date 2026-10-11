package com.hospital.notificacion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    private Long id;

    @Column("cita_id")
    private Long citaId;

    @Column("paciente_id")
    private Long pacienteId;

    private String tipo;
    private String mensaje;

    @Column("fecha_envio")
    private LocalDateTime fechaEnvio;
}