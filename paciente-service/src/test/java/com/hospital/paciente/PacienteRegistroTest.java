package com.hospital.paciente;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

@SpringBootTest
@AutoConfigureWebTestClient
class PacienteRegistroTest {

    @Autowired
    private WebTestClient webTestClient;

    private Map<String, Object> pacienteValido(String documento) {
        return Map.of(
                "tipoDocumento", "DNI",
                "numeroDocumento", documento,
                "nombres", "Ana",
                "apellidos", "Prueba Lopez",
                "fechaNacimiento", "1990-05-10",
                "telefono", "999000111",
                "correo", "ana@test.com");
    }

    @Test
    void registrarPacienteValido_devuelve201YEstadoActivo() {
        webTestClient.post().uri("/api/pacientes")
                .bodyValue(pacienteValido("70000001"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.estado").isEqualTo("ACTIVO");
    }

    @Test
    void registrarPacienteDuplicado_devuelve409() {
        webTestClient.post().uri("/api/pacientes")
                .bodyValue(pacienteValido("70000002"))
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post().uri("/api/pacientes")
                .bodyValue(pacienteValido("70000002"))
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void registrarPacienteSinNombres_devuelve400() {
        Map<String, Object> invalido = Map.of(
                "tipoDocumento", "DNI",
                "numeroDocumento", "70000003",
                "nombres", "",
                "apellidos", "Prueba");

        webTestClient.post().uri("/api/pacientes")
                .bodyValue(invalido)
                .exchange()
                .expectStatus().isBadRequest();
    }
}