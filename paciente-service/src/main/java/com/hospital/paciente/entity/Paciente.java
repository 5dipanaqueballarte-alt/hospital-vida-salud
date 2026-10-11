package com.hospital.paciente.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    private Long id;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Column("tipo_documento")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Column("numero_documento")
    private String numeroDocumento;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String telefono;

    @Email(message = "Correo inválido")
    private String correo;

    private String direccion;

    @Column("fecha_registro")
    private LocalDateTime fechaRegistro;

    private String estado;
}