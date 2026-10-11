package com.hospital.auditoria.repository;

import com.hospital.auditoria.entity.Auditoria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuditoriaRepository extends ReactiveCrudRepository<Auditoria, Long> {

    Flux<Auditoria> findByRecursoAndRecursoId(String recurso, Long recursoId);
}