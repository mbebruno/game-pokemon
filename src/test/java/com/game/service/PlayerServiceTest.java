package com.game.service;

import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.PlayerMapper;
import com.game.mapper.PlayerMapperImpl;
import com.game.model.dto.PlayerDto;
import com.game.model.entity.Game;
import com.game.model.entity.Player;
import com.game.model.entity.Pokemon;
import com.game.model.request.PlayerPokemonRequest;
import com.game.model.request.PlayerRequest;
import com.game.repository.GameRepository;
import com.game.repository.PlayerRepository;
import com.game.repository.PokemonRepository;
import com.game.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    private PlayerService playerService;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private PokemonRepository pokemonRepository;
    @Spy
    private PlayerMapper playerMapper=new PlayerMapperImpl();

    @BeforeEach
    void setUp() {
        playerService=new PlayerServiceImpl(playerRepository,gameRepository,pokemonRepository,playerMapper) ;
    }

    @Test
    @DisplayName("Test service to create player with correct parameters")
    void test_service_add_player() {

        Game game = Game.builder().id(1L).name("name").build();
        Player player = Player.builder().game(Game.builder().id(1L).build()).name("player").build();

        Mockito.when(this.gameRepository.findById(1L)).thenReturn(
                Optional.of(Game.builder().id(1L).name("name").build()));
        Mockito.when(this.playerRepository.save(player)).thenReturn(
                Player.builder().id(1L).name("player").build());

        PlayerDto actualPlayerDto = this.playerService.createPlayer(
                PlayerRequest.builder().gameId(1L).name("player").build());

        verify(this.gameRepository).findById(1L);
        verify(this.playerRepository).save(player);
        assertAll(() -> {
            assertEquals(1L, actualPlayerDto.getId());
            assertEquals("player", actualPlayerDto.getName());
        });
    }

    @Test
    @DisplayName("Test service to create player with no exist game")
    void test_service_add_player_no_exit_game() {

        PlayerRequest playerRequest=PlayerRequest.builder().gameId(1L).name("player").build();

        Mockito.when(this.gameRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertAll(() -> {
            NotFoundException exception= Assertions.assertThrows(NotFoundException.class,()->this.playerService
                    .createPlayer(playerRequest)
            );
        });
        verify(this.gameRepository).findById(1L);

    }



    @Test
    @DisplayName("Test service to create player with  2 players  game")
    void test_service_add_player_game_with_more_2_player() {

        PlayerRequest playerRequest=PlayerRequest.builder().gameId(1L).name("player").build();
        Player player = Player.builder().game(Game.builder().id(1L).build()).name("player").build();

        Mockito.when(this.gameRepository.findById(1L)).thenReturn(
                Optional.of(Game.builder().id(1L).name("name").build()));
        Mockito.when(this.playerRepository.save(player)).thenThrow(ApplicationException.class);

        assertAll(() -> {
            Assertions.assertThrows(ApplicationException.class,()->this.playerService.createPlayer(playerRequest));
        });
        verify(this.gameRepository).findById(1L);
        verify(this.playerRepository).save(player);

    }

    @Test
    @DisplayName("Test service to add pokemon to  player")
    void test_service_add_pokemon_to_player() {

        Pokemon pokemon=Pokemon.builder().id(1L).healthPoints(10).height(10).weight(10).name("pidgeot").build();
        Player player = Player.builder().id(1L).game(Game.builder().id(1L).name("name").build()).name("player").build();

        Mockito.when(this.playerRepository.findById(1L)).thenReturn(Optional.of(player));
        Mockito.when(this.pokemonRepository.findById(1L)).thenReturn(Optional.of(pokemon));
        Mockito.when(this.playerRepository.save(player)).thenAnswer(input->{
            player.setPokemon(pokemon);
            return player;
        });

        PlayerDto actualPlayerDto=this.playerService.addPokemonToPlayer(PlayerPokemonRequest.builder().playerId(1L).pokemonId(1L).build());

        verify(this.playerRepository).findById(1L);
        verify(this.pokemonRepository).findById(1L);
        verify(this.playerRepository).findById(1L);

        assertAll(() -> {
            assertEquals(1L, actualPlayerDto.getId());
            assertEquals("player", actualPlayerDto.getName());
            assertEquals(1L, actualPlayerDto.getPokemon().getId());
            assertEquals("pidgeot", actualPlayerDto.getPokemon().getName());
        });
    }

    @Test
    @DisplayName("Test service to add pokemon to  player with no exist player")
    void test_service_add_pokemon_to_player_no_exist_player() {

        PlayerPokemonRequest playerPokemonRequest= PlayerPokemonRequest.builder().playerId(1L).pokemonId(1L).build();

        Mockito.when(this.playerRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertAll(() -> {
            assertThrows(NotFoundException.class,()->this.playerService.addPokemonToPlayer(playerPokemonRequest));
        });
        verify(this.playerRepository).findById(1L);
    }

    @Test
    @DisplayName("Test service to add no exist pokemon to  player")
    void test_service_add_no_exist_pokemon_to_player() {
        PlayerPokemonRequest playerPokemonRequest=PlayerPokemonRequest.builder().playerId(1L).pokemonId(1L).build();
        Player player = Player.builder().id(1L).game(Game.builder().id(1L).name("name").build()).name("player").build();

        Mockito.when(this.playerRepository.findById(1L)).thenReturn(Optional.of(player));
        Mockito.when(this.pokemonRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertAll(() -> {
            assertThrows(NotFoundException.class,()->this.playerService.addPokemonToPlayer(playerPokemonRequest));
        });
        verify(this.playerRepository).findById(1L);
        verify(this.pokemonRepository).findById(1L);
    }

    @Test
    @DisplayName("Test service to list player")
    void test_service_list_player(){

        Mockito.when(this.playerRepository.findAll()).thenReturn(Arrays.asList(Player.builder().id(1L).name("playe1").build(),
                Player.builder().id(2L).name("player2").build()));

        List<PlayerDto> actualListPlayers=this.playerService.listPlayers();

        verify(this.playerRepository).findAll();
        assertThat(actualListPlayers, hasSize(2));


    }

}