package com.hospital.notificacion.repository;

import com.hospital.notificacion.entity.Notificacion;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NotificacionRepository extends ReactiveCrudRepository<Notificacion, Long> {

    Flux<Notificacion> findByPacienteId(Long pacienteId);
}