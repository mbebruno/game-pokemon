package com.game.service;

import com.game.model.dto.GameDto;
import com.game.model.request.GameRequest;

import java.util.List;

public interface GameService {
     GameDto createGame(final  GameRequest gameRequest);
     List<GameDto>listGames();
}
