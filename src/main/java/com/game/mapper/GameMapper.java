package com.game.mapper;


import com.game.model.dto.GameDto;
import com.game.model.entity.Game;
import com.game.model.request.GameRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface GameMapper {
    @Mapping(target = "id", source = "gameId")
    Game gameRequestToGame(GameRequest gameRequest);

    GameDto gameToGameDto(Game game);
}
