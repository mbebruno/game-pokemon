package com.game.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.PokemonMapper;
import com.game.model.dto.PokemonDto;
import com.game.model.dto.PokemonSummary;
import com.game.model.entity.Pokemon;
import com.game.model.request.PokemonRequest;
import com.game.repository.PokemonRepository;
import com.game.service.PokemonRemoteService;
import com.game.service.PokemonService;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.game.enums.ApplicationError.ERROR_FINDING_POKEMON;
import static com.game.enums.ApplicationError.POKEMON_REST_BIND_ERROR;

@AllArgsConstructor
@Service
public class PokemonServiceImpl implements PokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokemonMapper pokemonMapper;
    private final PokemonRemoteService pokemonRemoteService;

    /**
     *
     * load pokemon list from remote for each  get
     * details and save to database
     */
    @Override
    public String loadPokemonFromRemoteApi() {

        // Load list of 50 pokemon
        try {
           PokemonSummary pokemonSummary= this.pokemonRemoteService.getListOfPokemonFromRemote();
            pokemonSummary.getResults().stream().forEach(item->{
                //load details of each pokemon
                try {
                    PokemonDto pokemonDto = this.pokemonRemoteService.getOnePokemonFromRemote(item.getUrl());
                    this.pokemonRepository.save(pokemonMapper.pokemonDtoToPokemon(pokemonDto));
                } catch (JsonProcessingException e) {
                    throw new ApplicationException(POKEMON_REST_BIND_ERROR, e.getMessage());
                }

            });
        } catch (JsonProcessingException e) {
            throw  new ApplicationException(POKEMON_REST_BIND_ERROR, e.getMessage());
        }

        return Message.MSG_INITIALISE;

    }

    /**
     *
     * Configure HP of one pokemon
     * @param pokemonRequest of data we want to set into database
     * @return : details with all data of pokemon
     */
    @Override
    public PokemonDto configureHealthPoints(final PokemonRequest pokemonRequest,final long id) {
        //check if pokemon  exist
        Pokemon pokemon=this.pokemonRepository.findById(pokemonRequest.getPokemonId())
                .orElseThrow(()-> new NotFoundException(ERROR_FINDING_POKEMON, pokemonRequest.getPokemonId()));

        pokemon.setHealthPoints(pokemonRequest.getHealthPoints());

        return this.pokemonMapper.pokemonToPokemonDto(this.pokemonRepository.save(pokemon));
    }

    /**
     *
     * Get list of all pokemon
     * @return : list of pokemon
     */
    @Override
    public List<PokemonDto> listPokemon() {

        return this.pokemonRepository.findAll().stream()
                .map(this.pokemonMapper::pokemonToPokemonDto)
                .collect(Collectors.toList());
    }
}
