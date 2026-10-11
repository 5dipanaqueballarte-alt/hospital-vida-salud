package com.hospital.paciente.repository;

import com.hospital.paciente.entity.Paciente;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PacienteRepository extends ReactiveCrudRepository<Paciente, Long> {

    Mono<Boolean> existsByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);

    Flux<Paciente> findByEstado(String estado);
}