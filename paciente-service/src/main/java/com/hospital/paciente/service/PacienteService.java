package com.hospital.paciente.service;

import com.hospital.paciente.entity.Paciente;
import com.hospital.paciente.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public Flux<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Mono<Paciente> obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado: " + id)));
    }

    public Mono<Paciente> registrarPaciente(Paciente paciente) {
        return pacienteRepository
                .existsByTipoDocumentoAndNumeroDocumento(
                        paciente.getTipoDocumento(), paciente.getNumeroDocumento())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Ya existe un paciente con ese tipo y número de documento"));
                    }
                    paciente.setId(null);
                    paciente.setEstado("ACTIVO");
                    paciente.setFechaRegistro(LocalDateTime.now());
                    return pacienteRepository.save(paciente);
                })
                .onErrorMap(DuplicateKeyException.class, e ->
                        new ResponseStatusException(HttpStatus.CONFLICT, "Paciente duplicado"));
    }

    public Mono<Paciente> actualizar(Long id, Paciente datos) {
        return obtenerPorId(id).flatMap(actual -> {
            actual.setNombres(datos.getNombres());
            actual.setApellidos(datos.getApellidos());
            actual.setFechaNacimiento(datos.getFechaNacimiento());
            actual.setTelefono(datos.getTelefono());
            actual.setCorreo(datos.getCorreo());
            actual.setDireccion(datos.getDireccion());
            return pacienteRepository.save(actual);
        });
    }
}