package com.hospital.cita.service;

import com.hospital.cita.config.RabbitMQConfig;
import com.hospital.cita.entity.Cita;
import com.hospital.cita.event.CitaEvent;
import com.hospital.cita.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final WebClient.Builder webClientBuilder;
    private final RabbitTemplate rabbitTemplate;

    public Flux<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public Mono<Cita> obtenerPorId(Long id) {
        return citaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada: " + id)));
    }

    public Mono<Cita> programarCita(Cita cita) {
        return verificarPaciente(cita.getPacienteId())
                .then(Mono.defer(() -> citaRepository.existeChoque(
                        cita.getProfesionalCodigo(), cita.getFechaHora())))
                .flatMap(choque -> {
                    if (choque) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT, "Horario no disponible para el profesional"));
                    }
                    LocalDateTime ahora = LocalDateTime.now();
                    cita.setId(null);
                    cita.setEstado("PENDIENTE");
                    cita.setFechaCreacion(ahora);
                    cita.setFechaActualizacion(ahora);
                    return citaRepository.save(cita);
                });
    }

    public Mono<Cita> confirmar(Long id) {
        return cambiarEstado(id, "CONFIRMADA")
                .flatMap(c -> publicarEvento("cita.confirmada", c).thenReturn(c));
    }

    public Mono<Cita> cancelar(Long id) {
        return cambiarEstado(id, "CANCELADA")
                .flatMap(c -> publicarEvento("cita.cancelada", c).thenReturn(c));
    }

    public Mono<Cita> atender(Long id) {
        return cambiarEstado(id, "ATENDIDA");
    }

    private Mono<Cita> cambiarEstado(Long id, String nuevo) {
        return obtenerPorId(id).flatMap(cita -> {
            if (!transicionValida(cita.getEstado(), nuevo)) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                        "Transición no permitida: " + cita.getEstado() + " -> " + nuevo));
            }
            cita.setEstado(nuevo);
            cita.setFechaActualizacion(LocalDateTime.now());
            return citaRepository.save(cita);
        });
    }

    private boolean transicionValida(String actual, String nuevo) {
        return switch (actual) {
            case "PENDIENTE" -> nuevo.equals("CONFIRMADA") || nuevo.equals("CANCELADA");
            case "CONFIRMADA" -> nuevo.equals("ATENDIDA") || nuevo.equals("CANCELADA")
                    || nuevo.equals("AUSENTE");
            default -> false;
        };
    }

    private Mono<Void> verificarPaciente(Long pacienteId) {
        return webClientBuilder.build().get()
                .uri("http://paciente-service/api/pacientes/{id}", pacienteId)
                .retrieve()
                .onStatus(s -> s.value() == 404, r -> Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El paciente no existe: " + pacienteId)))
                .bodyToMono(PacienteResponse.class)
                .timeout(Duration.ofSeconds(3))
                .onErrorMap(e -> !(e instanceof ResponseStatusException),
                        e -> new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                                "Servicio de pacientes no disponible"))
                .flatMap(p -> "ACTIVO".equals(p.estado())
                        ? Mono.<Void>empty()
                        : Mono.error(new ResponseStatusException(
                        HttpStatus.CONFLICT, "El paciente está inactivo")));
    }

    private Mono<Void> publicarEvento(String routingKey, Cita cita) {
        CitaEvent evento = new CitaEvent(cita.getId(), cita.getPacienteId(),
                cita.getFechaHora(), cita.getEstado(), LocalDateTime.now());
        return Mono.fromRunnable(() ->
                        rabbitTemplate.convertAndSend(
                                RabbitMQConfig.EXCHANGE_CITAS, routingKey, evento))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    record PacienteResponse(Long id, String estado) {}
}