package com.example.movements.movements.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movement {

    @Id
    @Column(name = "movement_id", length = 30)
    private String movementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Simplificación: no necesitamos la entidad Transfer para este endpoint
    @Column(name = "transfer_id", length = 30)
    private String transferId;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "type", nullable = false, length = 30)
    private String type; // IN | OUT

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "movement_dt", nullable = false)
    private LocalDateTime movementDt;
}