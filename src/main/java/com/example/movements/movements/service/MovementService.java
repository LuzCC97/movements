package com.example.movements.movements.service;

import com.example.movements.movements.dto.MovementListResponse;

public interface MovementService {
    MovementListResponse getMovementsByAccount(String accountId);
}