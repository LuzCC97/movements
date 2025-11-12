package com.example.movements.movements.mapper;

import com.example.movements.movements.dto.MovementDto;
import com.example.movements.movements.entity.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @Mappings({
            @Mapping(source = "movementId", target = "movementId"),
            @Mapping(source = "transferId", target = "transferId"),
            @Mapping(target = "amount", expression = "java(m.getAmount() != null ? m.getAmount().doubleValue() : null)"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "description", target = "description")
    })
    MovementDto toDto(Movement m);

    List<MovementDto> toDtoList(List<Movement> list);
}