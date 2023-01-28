package com.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.game.model.dto.PokemonDto;
import com.game.model.dto.PokemonSummary;

public interface PokemonRemoteService {

    PokemonSummary getListOfPokemonFromRemote() throws JsonProcessingException;
    PokemonDto getOnePokemonFromRemote(String url) throws JsonProcessingException;
}
