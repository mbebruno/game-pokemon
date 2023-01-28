package com.game.service;

import com.game.model.dto.PokemonDto;
import com.game.model.request.PokemonRequest;

import java.util.List;

public interface PokemonService {
    String loadPokemonFromRemoteApi();
    PokemonDto configureHealthPoints(final PokemonRequest pokemonRequest,final long id);
    List<PokemonDto> listPokemon();
}
