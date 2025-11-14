package com.example.movements.movements.mapper;

import com.example.movements.movements.dto.MovementListResponse;
import com.example.movements.movements.entity.Account;
import com.example.movements.movements.entity.Movement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        MovementListResponseMapperImpl.class,
        MovementMapperImpl.class
})
class MovementListResponseMapperTest {

    @Autowired
    private MovementListResponseMapper mapper;

    @Test
    void toResponse_maps_account_and_movements() {
        Account acc = new Account("ACC-9", "USD", new BigDecimal("250.75"), "ACTIVE");
        Movement m1 = new Movement("M1", acc, "T1", new BigDecimal("10.00"), "IN", "A", LocalDateTime.now());
        Movement m2 = new Movement("M2", acc, "T2", new BigDecimal("20.50"), "OUT", "B", LocalDateTime.now());

        MovementListResponse resp = mapper.toResponse(acc, List.of(m1, m2));

        assertThat(resp.getAccountId()).isEqualTo("ACC-9");         // línea 16 del mapper
        assertThat(resp.getCurrency()).isEqualTo("USD");             // línea 17
        assertThat(resp.getBalance()).isEqualTo(250.75);             // línea 19 (expression BigDecimal->Double)
        assertThat(resp.getMovements()).hasSize(2);                  // línea 21 (usa MovementMapper)
        assertThat(resp.getMovements().get(0).getMovementId()).isEqualTo("M1");
    }

    @Test
    void toResponse_handles_null_balance() {
        Account acc = new Account("ACC-10", "PEN", null, "ACTIVE");

        MovementListResponse resp = mapper.toResponse(acc, List.of());

        assertThat(resp.getBalance()).isNull(); // cubre branch null-safe de la expression en línea 19
    }
    @Test
    void toResponse_whenMovementsIsNull_setsMovementsNull_andMapsAccountFields() {
        Account acc = new Account("ACC-11", "EUR", new BigDecimal("10.00"), "ACTIVE");

        MovementListResponse resp = mapper.toResponse(acc, null);

        assertThat(resp).isNotNull();
        assertThat(resp.getAccountId()).isEqualTo("ACC-11");
        assertThat(resp.getCurrency()).isEqualTo("EUR");
        assertThat(resp.getBalance()).isEqualTo(10.00);
        assertThat(resp.getMovements()).isNull(); // lista origen null -> destino null
    }

    @Test
    void toResponse_whenMovementsIsEmpty_setsEmptyList() {
        Account acc = new Account("ACC-12", "PEN", new BigDecimal("5.00"), "ACTIVE");

        MovementListResponse resp = mapper.toResponse(acc, List.of());

        assertThat(resp).isNotNull();
        assertThat(resp.getMovements()).isNotNull();
        assertThat(resp.getMovements()).isEmpty();
    }

    @Test
    void toResponse_whenAccountIsNull_throwsNpe() {
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
                mapper.toResponse(null, List.of())
        );
    }
    @Test
    void toResponse_whenAccountAndMovementsAreNull_returnsNull() {
        MovementListResponse resp = mapper.toResponse(null, null);
        // Cubre la rama: if (account == null && movements == null) return null;
        assertThat(resp).isNull();
    }
}