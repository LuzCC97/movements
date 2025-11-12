package com.example.movements.movements.mapper;

import com.example.movements.movements.dto.MovementListResponse;
import com.example.movements.movements.entity.Account;
import com.example.movements.movements.entity.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = { MovementMapper.class })
public interface MovementListResponseMapper {

    @Mappings({
            @Mapping(source = "account.accountId", target = "accountId"),
            @Mapping(source = "account.currency",  target = "currency"),
            // BigDecimal -> Double con expression para evitar NPE
            @Mapping(target = "balance", expression = "java(account.getBalance() != null ? account.getBalance().doubleValue() : null)"),
            // usa MovementMapper para mapear la lista automáticamente
            @Mapping(source = "movements", target = "movements")
    })
    MovementListResponse toResponse(Account account, List<Movement> movements);
}