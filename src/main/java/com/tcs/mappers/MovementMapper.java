package com.tcs.mappers;

import com.tcs.dto.MovementDTO;
import com.tcs.model.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovementMapper {

    MovementMapper INSTANCE = Mappers.getMapper(MovementMapper.class);

    MovementDTO toMovementDTO(MovementEntity movement);

    MovementEntity toMovement(MovementDTO movementDTO);
}
