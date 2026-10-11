package com.hospital.auditoria.listener;

import com.hospital.auditoria.config.RabbitConfig;
import com.hospital.auditoria.entity.Auditoria;
import com.hospital.auditoria.event.CitaEvent;
import com.hospital.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditoriaListener {

    private final AuditoriaRepository auditoriaRepository;

    @RabbitListener(queues = RabbitConfig.COLA_AUDITORIA)
    public void registrarEvento(CitaEvent evento) {
        Auditoria registro = new Auditoria(
                null,
                "CITA",
                evento.citaId(),
                evento.estado(),
                "cita." + evento.estado().toLowerCase(),
                "cita-service",
                LocalDateTime.now());

        auditoriaRepository.save(registro)
                .doOnSuccess(a -> log.info("Auditoría registrada: CITA {} -> {}", a.getRecursoId(), a.getEstadoNuevo()))
                .doOnError(e -> log.error("Error al registrar auditoría", e))
                .subscribe();
    }
}