package com.game.service;

import com.game.model.dto.PlayerDto;
import com.game.model.request.PlayerPokemonRequest;
import com.game.model.request.PlayerRequest;

import java.util.List;

public interface PlayerService {
     PlayerDto createPlayer(final PlayerRequest playerRequest);
      PlayerDto addPokemonToPlayer(final PlayerPokemonRequest playerPokemonRequest);
     List<PlayerDto>listPlayers();
}
