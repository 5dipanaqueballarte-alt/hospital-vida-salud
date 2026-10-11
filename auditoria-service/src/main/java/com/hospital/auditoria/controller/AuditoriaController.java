package com.hospital.auditoria.controller;

import com.hospital.auditoria.entity.Auditoria;
import com.hospital.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    @GetMapping
    public Flux<Auditoria> listar() {
        return auditoriaRepository.findAll();
    }

    @GetMapping("/{recurso}/{recursoId}")
    public Flux<Auditoria> porRecurso(@PathVariable String recurso, @PathVariable Long recursoId) {
        return auditoriaRepository.findByRecursoAndRecursoId(recurso.toUpperCase(), recursoId);
    }
}