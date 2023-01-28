package com.game.mapper;

import com.game.model.dto.PokemonDto;
import com.game.model.entity.Pokemon;
import org.mapstruct.Mapper;

@Mapper
public interface PokemonMapper {

    Pokemon pokemonDtoToPokemon(PokemonDto pokemonDto);
    PokemonDto pokemonToPokemonDto(Pokemon pokemon);

}
