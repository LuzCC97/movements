package com.example.movements.movements.repository;

import com.example.movements.movements.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}