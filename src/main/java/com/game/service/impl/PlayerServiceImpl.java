package com.game.service.impl;

import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.PlayerMapper;
import com.game.model.dto.PlayerDto;
import com.game.model.entity.Game;
import com.game.model.entity.Player;
import com.game.model.entity.Pokemon;
import com.game.model.request.PlayerPokemonRequest;
import com.game.model.request.PlayerRequest;
import com.game.repository.GameRepository;
import com.game.repository.PlayerRepository;
import com.game.repository.PokemonRepository;
import com.game.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.game.enums.ApplicationError.*;

@AllArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final PokemonRepository pokemonRepository;
    private final PlayerMapper playerMapper;

    /**
     *
     * Create player
     * @param playerRequest of data we want to create player
     * @return : details with all data of player
     */
    @Override
    public PlayerDto createPlayer(final PlayerRequest playerRequest){
        //check if game exist
       Game game=this.gameRepository.findById(playerRequest.getGameId())
               .orElseThrow(()-> new NotFoundException(ERROR_FINDING_GAME, playerRequest.getGameId()));

        //check if players numbers is not more 2
        if(game.getPlayers()!=null &&  game.getPlayers().size()>=2)
            throw  new ApplicationException(ERROR_LIMIT_GAME_PLAYER, playerRequest.getGameId());

        return this.playerMapper.playerToPlayerDto(this.playerRepository.save(
                this.playerMapper.playerRequestToPlayer(playerRequest)));
    }

    /**
     *
     * Add selected pokemon to player
     * @param playerPokemonRequest of data we want to create player
     * @return : details with all data of player
     */

    public PlayerDto addPokemonToPlayer(final PlayerPokemonRequest playerPokemonRequest){
        //check if player exist
        Player player=this.playerRepository.findById(playerPokemonRequest.getPlayerId())
                .orElseThrow(()-> new NotFoundException(ERROR_FINDING_PLAYER, playerPokemonRequest.getPlayerId()));

        //check if pokemon exist
        Pokemon pokemon=this.pokemonRepository.findById(playerPokemonRequest.getPlayerId())
                .orElseThrow(()-> new NotFoundException(ERROR_FINDING_POKEMON, playerPokemonRequest.getPokemonId()));

        player.setPokemon(pokemon);

        return this.playerMapper.playerToPlayerDto(this.playerRepository.save(player));
    }




    /**
     *
     * get list of all player
     * @return : all player lst data
     */

    @Override
    public List<PlayerDto> listPlayers(){

        return this.playerRepository.findAll().stream()
                .map(this.playerMapper::playerToPlayerDto)
                .collect(Collectors.toList());
    }
}
