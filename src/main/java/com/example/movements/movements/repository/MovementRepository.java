package com.example.movements.movements.repository;

import com.example.movements.movements.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, String> {

    List<Movement> findByAccount_AccountIdOrderByMovementDtAsc(String accountId);
}