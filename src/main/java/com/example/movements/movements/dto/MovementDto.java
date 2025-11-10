package com.example.movements.movements.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovementDto {
    private String movementId;
    private String transferId;
    private Double amount;
    private String type;       // IN | OUT
    private String description;
}
