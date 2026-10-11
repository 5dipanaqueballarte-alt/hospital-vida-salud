package com.hospital.cita.controller;

import com.hospital.cita.entity.Cita;
import com.hospital.cita.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    public Flux<Cita> listar() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public Mono<Cita> obtener(@PathVariable Long id) {
        return citaService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cita> crear(@Valid @RequestBody Cita cita) {
        return citaService.programarCita(cita);
    }

    @PutMapping("/{id}/confirmar")
    public Mono<Cita> confirmar(@PathVariable Long id) {
        return citaService.confirmar(id);
    }

    @PutMapping("/{id}/cancelar")
    public Mono<Cita> cancelar(@PathVariable Long id) {
        return citaService.cancelar(id);
    }

    @PutMapping("/{id}/atender")
    public Mono<Cita> atender(@PathVariable Long id) {
        return citaService.atender(id);
    }
}