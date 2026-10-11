package com.hospital.paciente.controller;

import com.hospital.paciente.entity.Paciente;
import com.hospital.paciente.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    public Flux<Paciente> listar() {
        return pacienteService.listarPacientes();
    }

    @GetMapping("/{id}")
    public Mono<Paciente> obtenerPorId(@PathVariable Long id) {
        return pacienteService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Paciente> crear(@Valid @RequestBody Paciente paciente) {
        return pacienteService.registrarPaciente(paciente);
    }

    @PutMapping("/{id}")
    public Mono<Paciente> actualizar(@PathVariable Long id, @Valid @RequestBody Paciente paciente) {
        return pacienteService.actualizar(id, paciente);
    }
}