package com.game.mapper;

import com.game.model.dto.RoundDto;
import com.game.model.entity.Round;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoundMapper {


    @Mapping( target = "battle.rounds", ignore = true)

    @Mapping( target = "winner.game", ignore = true)
    RoundDto roundToRoundDto(Round round);
}
