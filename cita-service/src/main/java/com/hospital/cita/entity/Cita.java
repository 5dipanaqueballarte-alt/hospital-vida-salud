package com.hospital.cita.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    private Long id;

    @NotNull(message = "El paciente es obligatorio")
    @Column("paciente_id")
    private Long pacienteId;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Column("fecha_hora")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El profesional es obligatorio")
    @Column("profesional_codigo")
    private String profesionalCodigo;

    private String motivo;

    // PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA, AUSENTE
    private String estado;

    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}