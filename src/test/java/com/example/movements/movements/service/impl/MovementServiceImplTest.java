package com.example.movements.movements.service.impl;

import com.example.movements.movements.dto.MovementListResponse;
import com.example.movements.movements.entity.Account;
import com.example.movements.movements.entity.Movement;
import com.example.movements.movements.exception.ResourceNotFoundException;
import com.example.movements.movements.mapper.MovementListResponseMapper;
import com.example.movements.movements.repository.AccountRepository;
import com.example.movements.movements.repository.MovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//Mock de repos y mapper; no se levanta Spring.
//Verificación de interacciones esperadas.
//Mensaje de excepción alineado con MovementServiceImpl.

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private MovementListResponseMapper movementListResponseMapper;

    @InjectMocks
    private MovementServiceImpl service;

    @Test
    void getMovementsByAccount_ok() {
        // given
        String accountId = "ACC-123";
        Account acc = new Account(accountId, "PEN", new BigDecimal("1000.50"), "ACTIVE");

        Movement mov1 = new Movement(
                "MOV-1",
                acc,
                "TR-1",
                new BigDecimal("50.00"),
                "IN",
                "Abono",
                LocalDateTime.now().minusDays(2)
        );
        Movement mov2 = new Movement(
                "MOV-2",
                acc,
                "TR-2",
                new BigDecimal("20.00"),
                "OUT",
                "Pago",
                LocalDateTime.now().minusDays(1)
        );
        List<Movement> movements = List.of(mov1, mov2);

        MovementListResponse expected = new MovementListResponse(
                accountId,
                "PEN",
                1000.50,
                List.of(
                        // No es necesario validar el mapeo interno aquí; se prueba el contrato del servicio
                        new com.example.movements.movements.dto.MovementDto("MOV-1", "TR-1", 50.00, "IN", "Abono"),
                        new com.example.movements.movements.dto.MovementDto("MOV-2", "TR-2", 20.00, "OUT", "Pago")
                )
        );

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(acc));
        when(movementRepository.findByAccount_AccountIdOrderByMovementDtAsc(accountId)).thenReturn(movements);
        when(movementListResponseMapper.toResponse(acc, movements)).thenReturn(expected);

        // when
        MovementListResponse result = service.getMovementsByAccount(accountId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(accountId);
        assertThat(result.getCurrency()).isEqualTo("PEN");
        assertThat(result.getBalance()).isEqualTo(1000.50);
        assertThat(result.getMovements()).hasSize(2);

        verify(accountRepository).findById(accountId);
        verify(movementRepository).findByAccount_AccountIdOrderByMovementDtAsc(accountId);
        verify(movementListResponseMapper).toResponse(acc, movements);
        verifyNoMoreInteractions(accountRepository, movementRepository, movementListResponseMapper);
    }

    @Test
    void getMovementsByAccount_accountNotFound_throws() {
        // given
        String accountId = "ACC-404";
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // when
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getMovementsByAccount(accountId)
        );

        // then
        assertThat(ex.getMessage()).contains("Cuenta ID " + accountId + " no encontrada.");
        verify(accountRepository).findById(accountId);
        verifyNoInteractions(movementRepository, movementListResponseMapper);
        verifyNoMoreInteractions(accountRepository);
    }
}