package com.example.movements.movements.controller;

import com.example.movements.movements.dto.MovementDto;
import com.example.movements.movements.dto.MovementListResponse;
import com.example.movements.movements.service.MovementService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Pruebas del controlador con MockMvc
//Valida: Contrato HTTP del endpoint y manejo de excepciones a través del controlador y el advice global.

//WebMvcTest: carga solo la capa web. Con [MockBean](cci:4://file://MockBean:0:0-0:0) MovementService y MockMvc.
@WebMvcTest(MovementController.class)
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //MockBean:para el MovementService.
    @MockBean
    private MovementService movementService;
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
                .andExpect(jsonPath("$.accountId", is(accountId)))
                .andExpect(jsonPath("$.currency", is("PEN")))
                .andExpect(jsonPath("$.balance", is(1000.50)))
                .andExpect(jsonPath("$.movements", hasSize(2)))
                .andExpect(jsonPath("$.movements[0].movementId", is("MOV-1")));
    }

    // simula ResourceNotFoundException y verifica 404 + cuerpo con error
    // y message como define el GlobalExceptionHandler.
    @Test
    void getMovements_whenServiceThrowsNotFound_returns404_withBody() throws Exception {
        String accountId = "ACC-404";
        // simulate service throwing exception handled by GlobalExceptionHandler (GlobalExceptionHandler.java líneas 14–20)
        BDDMockito.given(movementService.getMovementsByAccount(accountId))
                .willThrow(new com.example.movements.movements.exception.ResourceNotFoundException("Cuenta ID " + accountId + " no encontrada."));

        mockMvc.perform(get("/account/{accountId}/movements", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("cuenta no encontrada")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(accountId)));
    }

    //simula RuntimeException y verifica 500 + cuerpo con error y message.
    @Test
    void getMovements_whenServiceThrowsGeneric_returns500_withBody() throws Exception {
        String accountId = "ACC-500";
        // simulate generic exception handled by GlobalExceptionHandler (GlobalExceptionHandler.java líneas 22–28)
        BDDMockito.given(movementService.getMovementsByAccount(accountId))
                .willThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/account/{accountId}/movements", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("error interno")))
                .andExpect(jsonPath("$.message", is("boom")));
    }
}