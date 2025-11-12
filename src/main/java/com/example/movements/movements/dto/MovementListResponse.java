package com.example.movements.movements.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovementListResponse {
    private String accountId;
    private String currency;
    private Double balance;
    private List<MovementDto> movements;
}
