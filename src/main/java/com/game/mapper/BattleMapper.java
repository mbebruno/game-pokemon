package com.game.mapper;

import com.game.model.dto.BattleDto;
import com.game.model.entity.Battle;
import com.game.model.request.BattleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BattleMapper {

    @Mapping(target = "id", source = "battleId")
    @Mapping(target = "game.id", source = "gameId")
    Battle battleRequestToBattle(BattleRequest battleRequest);
    @Mapping( target = "game.players", ignore = true)
    BattleDto battleToBattleDto(Battle battle);
}
