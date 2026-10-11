package com.hospital.cita.repository;

import com.hospital.cita.entity.Cita;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface CitaRepository extends ReactiveCrudRepository<Cita, Long> {

    @Query("SELECT COUNT(*) > 0 FROM citas WHERE profesional_codigo = :profesional " +
            "AND fecha_hora = :fechaHora AND estado <> 'CANCELADA'")
    Mono<Boolean> existeChoque(String profesional, LocalDateTime fechaHora);

    Flux<Cita> findByPacienteId(Long pacienteId);

    Flux<Cita> findByEstado(String estado);
}