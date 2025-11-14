package com.example.movements.movements.controller;

import com.example.movements.movements.dto.MovementDto;
import com.example.movements.movements.dto.MovementListResponse;
import com.example.movements.movements.service.MovementService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Pruebas del controlador con MockMvc
//Valida: Contrato HTTP del endpoint y manejo de excepciones a través del controlador y el advice global.

//WebMvcTest: carga solo la capa web. Con [MockBean](cci:4://file://MockBean:0:0-0:0) MovementService y MockMvc.
@WebMvcTest(MovementController.class)
@Import(MovementControllerTest.TestConfig.class)
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //MockBean:para el MovementService.
    @Autowired
    private MovementService movementService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        MovementService movementService() {
            return Mockito.mock(MovementService.class);
        }
    }

    //happy path. Mockea el servicio y verifica status 200 y cuerpo JSON (accountId, currency, balance, lista).
    @Test
    void getMovements_returns200_andBody() throws Exception {
        String accountId = "ACC-123";
        MovementListResponse response = new MovementListResponse(
                accountId,
                "PEN",
                1000.50,
                List.of(
                        new MovementDto("MOV-1", "TR-1", 50.0, "IN", "Abono"),
                        new MovementDto("MOV-2", "TR-2", 20.0, "OUT", "Pago")
                )
        );

        BDDMockito.given(movementService.getMovementsByAccount(accountId)).willReturn(response);

        mockMvc.perform(get("/account/{accountId}/movements", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId", equalTo(accountId)))
                .andExpect(jsonPath("$.currency", equalTo("PEN")))
                .andExpect(jsonPath("$.balance", equalTo(1000.50)))
                .andExpect(jsonPath("$.movements", hasSize(2)))
                .andExpect(jsonPath("$.movements[0].movementId", equalTo("MOV-1")));
    }

}