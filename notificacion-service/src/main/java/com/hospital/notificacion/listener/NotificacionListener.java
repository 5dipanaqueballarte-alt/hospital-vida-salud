package com.hospital.notificacion.listener;

import com.hospital.notificacion.config.RabbitConfig;
import com.hospital.notificacion.entity.Notificacion;
import com.hospital.notificacion.event.CitaEvent;
import com.hospital.notificacion.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacionListener {

    private final NotificacionRepository notificacionRepository;

    @RabbitListener(queues = RabbitConfig.COLA_NOTIFICACIONES)
    public void recibirEventoCita(CitaEvent evento) {
        log.info("Evento de cita recibido: citaId={}, estado={}", evento.citaId(), evento.estado());

        Notificacion notificacion = new Notificacion(
                null,
                evento.citaId(),
                evento.pacienteId(),
                evento.estado(),
                "Su cita " + evento.citaId() + " cambió a estado " + evento.estado(),
                LocalDateTime.now());

        notificacionRepository.save(notificacion)
                .doOnSuccess(n -> log.info("Notificación guardada id={}", n.getId()))
                .doOnError(e -> log.error("Error al guardar notificación", e))
                .subscribe();
    }
}