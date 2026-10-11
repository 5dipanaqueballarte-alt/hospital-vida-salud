package com.hospital.auditoria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auditoria {

    @Id
    private Long id;

    private String recurso;

    @Column("recurso_id")
    private Long recursoId;

    @Column("estado_nuevo")
    private String estadoNuevo;

    private String evento;
    private String origen;
    private LocalDateTime fecha;
}