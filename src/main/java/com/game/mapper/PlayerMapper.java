package com.game.mapper;


import com.game.model.dto.PlayerDto;
import com.game.model.entity.Player;
import com.game.model.request.PlayerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "id", source = "playerId")
    @Mapping(target = "game.id", source = "gameId")
    Player playerRequestToPlayer(PlayerRequest playerRequest);
    @Mapping( target = "game.players", ignore = true)
    PlayerDto playerToPlayerDto(Player player);
}
