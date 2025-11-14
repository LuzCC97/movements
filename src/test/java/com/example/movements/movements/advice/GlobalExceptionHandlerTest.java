package com.example.movements.movements.advice;

import com.example.movements.movements.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404_andBody() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Cuenta ID ACC-404 no encontrada.");

        ResponseEntity<Map<String, String>> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .containsEntry("error", "cuenta no encontrada");
        assertThat(response.getBody().get("message"))
                .contains("ACC-404");
    }

    @Test
    void handleGeneric_returns500_andBody() {
        Exception ex = new RuntimeException("boom");

        ResponseEntity<Map<String, String>> response = handler.handleGeneric(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .containsEntry("error", "error interno");
        assertThat(response.getBody().get("message"))
                .isEqualTo("boom");
    }
}