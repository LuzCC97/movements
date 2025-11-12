package com.example.movements.movements.service.impl;

import com.example.movements.movements.dto.MovementListResponse;
import com.example.movements.movements.entity.Account;
import com.example.movements.movements.entity.Movement;
import com.example.movements.movements.exception.ResourceNotFoundException;
import com.example.movements.movements.mapper.MovementListResponseMapper;
import com.example.movements.movements.mapper.MovementMapper;
import com.example.movements.movements.repository.AccountRepository;
import com.example.movements.movements.repository.MovementRepository;
import com.example.movements.movements.service.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final MovementListResponseMapper movementListResponseMapper;

    @Override
    public MovementListResponse getMovementsByAccount(String accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta ID " + accountId + " no encontrada."));

        List<Movement> movements = movementRepository
                .findByAccount_AccountIdOrderByMovementDtAsc(accountId);

        return movementListResponseMapper.toResponse(acc, movements);
    }
}