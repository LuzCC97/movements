package com.example.movements.movements.mapper;

import com.example.movements.movements.dto.MovementDto;
import com.example.movements.movements.entity.Account;
import com.example.movements.movements.entity.Movement;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MovementMapperTest {

    private final MovementMapper mapper = Mappers.getMapper(MovementMapper.class);

    @Test
    void toDto_maps_all_fields_and_amount_conversion() {
        Account acc = new Account("ACC-1", "PEN", new BigDecimal("100.00"), "ACTIVE");
        Movement m = new Movement(
                "MOV-1",
                acc,
                "TR-1",
                new BigDecimal("50.25"),
                "IN",
                "Abono",
                LocalDateTime.now()
        );

        MovementDto dto = mapper.toDto(m);

        assertThat(dto.getMovementId()).isEqualTo("MOV-1");
        assertThat(dto.getTransferId()).isEqualTo("TR-1");
        assertThat(dto.getAmount()).isEqualTo(50.25);
        assertThat(dto.getType()).isEqualTo("IN");
        assertThat(dto.getDescription()).isEqualTo("Abono");
    }

    @Test
    void toDto_handles_null_amount() {
        Account acc = new Account("ACC-1", "PEN", new BigDecimal("100.00"), "ACTIVE");
        Movement m = new Movement(
                "MOV-2",
                acc,
                "TR-2",
                null, // amount null -> debe mapear a null sin NPE (MovementMapper.java líneas 17–19)
                "OUT",
                "Pago",
                LocalDateTime.now()
        );

        MovementDto dto = mapper.toDto(m);

        assertThat(dto.getAmount()).isNull();
        assertThat(dto.getType()).isEqualTo("OUT");
    }

    @Test
    void toDtoList_maps_list() {
        Account acc = new Account("ACC-1", "PEN", new BigDecimal("100.00"), "ACTIVE");
        Movement m1 = new Movement("M1", acc, "T1", new BigDecimal("10.00"), "IN", "A", LocalDateTime.now());
        Movement m2 = new Movement("M2", acc, "T2", new BigDecimal("20.00"), "OUT", "B", LocalDateTime.now());

        List<MovementDto> list = mapper.toDtoList(List.of(m1, m2));

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getMovementId()).isEqualTo("M1");
        assertThat(list.get(1).getMovementId()).isEqualTo("M2");
    }
    @Test
    void toDto_whenSourceIsNull_returnsNull() {
        MovementDto dto = mapper.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void toDtoList_whenSourceIsNull_returnsNull() {
        List<MovementDto> list = mapper.toDtoList(null);
        assertThat(list).isNull();
    }

    @Test
    void toDtoList_whenSourceIsEmpty_returnsEmptyList() {
        List<MovementDto> list = mapper.toDtoList(List.of());
        assertThat(list).isNotNull();
        assertThat(list).isEmpty();
    }
}