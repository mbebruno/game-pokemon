package com.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.PokemonMapper;
import com.game.mapper.PokemonMapperImpl;
import com.game.model.dto.PokemonDto;
import com.game.model.dto.PokemonSummary;
import com.game.model.entity.Pokemon;
import com.game.model.request.PokemonRequest;
import com.game.repository.PokemonRepository;
import com.game.service.impl.PokemonServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    private PokemonService pokemonService;
    @Mock
    private PokemonRepository pokemonRepository;

    @Spy
    private PokemonMapper pokemonMapper=new PokemonMapperImpl();

    @Mock
    private  PokemonRemoteService pokemonRemoteService;

    @BeforeEach
    void setUp() {
        pokemonService=new PokemonServiceImpl(pokemonRepository,pokemonMapper,pokemonRemoteService) ;
    }

    @Test
    @DisplayName("Test service to load list of pokemon")
    void test_service_load_pokemon() throws JsonProcessingException {

        List<Pokemon> results=Arrays.asList(
                Pokemon.builder().id(1L).healthPoints(0).height(10).weight(10).name("pidgeot").build(),
                Pokemon.builder().id(2L).healthPoints(0).height(100).weight(1000).name("raichu").build()
        );
        List<PokemonDto> resultsDto=Arrays.asList(
                PokemonDto.builder().id(1L).healthPoints(0).height(10).weight(10).name("pidgeot").build(),
                PokemonDto.builder().id(2L).healthPoints(0).height(100).weight(1000).name("raichu").build()
                );
        PokemonSummary pokemonSummary=PokemonSummary.builder().results(Arrays.asList(
                PokemonDto.builder().id(1L).url("url1").name("pidgeot").build(),
                PokemonDto.builder().id(2L).url("url1").name("raichu").build())
        ).build();

        Mockito.when(this.pokemonRemoteService.getListOfPokemonFromRemote()).thenReturn(pokemonSummary);
        Mockito.when(this.pokemonRemoteService.getOnePokemonFromRemote(any(String.class))).thenReturn(resultsDto.get(0),resultsDto.get(1));
        Mockito.when(this.pokemonRepository.save(any(Pokemon.class))).thenReturn(results.get(0),results.get(1));

        this.pokemonService.loadPokemonFromRemoteApi();

        verify(this.pokemonRemoteService).getListOfPokemonFromRemote();
        verify(this.pokemonRemoteService,times(2)).getOnePokemonFromRemote(any(String.class));
        verify(this.pokemonRepository,times(2)).save(any(Pokemon.class));

    }

    @Test
    @DisplayName("Test service to load list of pokemon with trow pokemon details exception ")
    void test_service_load_pokemon_detail_exception() throws JsonProcessingException {

        PokemonSummary pokemonSummary=PokemonSummary.builder().results(Arrays.asList(
                PokemonDto.builder().id(1L).url("url1").name("pidgeot").build(),
                PokemonDto.builder().id(2L).url("url1").name("raichu").build())
        ).build();

        Mockito.when(this.pokemonRemoteService.getListOfPokemonFromRemote()).thenReturn(pokemonSummary);
        Mockito.when(this.pokemonRemoteService.getOnePokemonFromRemote(any(String.class))).thenThrow(ApplicationException.class);

        assertAll(() -> {
            Assertions.assertThrows(ApplicationException.class,()->this.pokemonService.loadPokemonFromRemoteApi()
            );
        });
        verify(this.pokemonRemoteService).getListOfPokemonFromRemote();
        verify(this.pokemonRemoteService,times(1)).getOnePokemonFromRemote(any(String.class));
        verify(this.pokemonRepository,times(0)).save(any(Pokemon.class));

    }

    @Test
    @DisplayName("Test service to load list of pokemon with  summary exception")
    void test_service_load_pokemon_summary_exception() throws JsonProcessingException {

        Mockito.when(this.pokemonRemoteService.getListOfPokemonFromRemote()).thenThrow(ApplicationException.class);

        assertAll(() -> {
            Assertions.assertThrows(ApplicationException.class,()->this.pokemonService.loadPokemonFromRemoteApi()
            );
        });
        verify(this.pokemonRemoteService).getListOfPokemonFromRemote();
        verify(this.pokemonRemoteService,times(0)).getOnePokemonFromRemote(any(String.class));
        verify(this.pokemonRepository,times(0)).save(any(Pokemon.class));

    }

    @Test
    @DisplayName("Test service to configure HP of pokemon")
    void test_service_configure_pokemon() {

        Pokemon pokemon=Pokemon.builder().id(1L).healthPoints(0).height(10).weight(10).name("pidgeot").build();

        Mockito.when(this.pokemonRepository.findById(1L)).thenReturn(
                Optional.of(pokemon));
        Mockito.when(this.pokemonRepository.save(pokemon)).thenReturn(pokemon);

        PokemonDto actualPokemonDto = this.pokemonService.configureHealthPoints(PokemonRequest.builder()
                .pokemonId(1L).healthPoints(10).build(),1L);

        verify(this.pokemonRepository).findById(1L);
        verify(this.pokemonRepository).save(pokemon);
        assertAll(() -> {
            assertEquals(1L, actualPokemonDto.getId());
            assertEquals(10, actualPokemonDto.getHealthPoints());
        });

    }

    @Test
    @DisplayName("Test service to configure HP of no exist pokemon ")
    void test_service_configure_no_exist_pokemon() {

        PokemonRequest pokemonRequest=PokemonRequest.builder().pokemonId(1L).healthPoints(10).build();

        Mockito.when(this.pokemonRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertAll(() -> {
            Assertions.assertThrows(NotFoundException.class,()->
                    this.pokemonService.configureHealthPoints(pokemonRequest,1L));
        });
        verify(this.pokemonRepository).findById(1L);

    }

    @Test
    @DisplayName("Test service to list pokemon")
    void test_service_list_pokemon() {
        List<Pokemon> results = Arrays.asList(
                Pokemon.builder().id(1L).healthPoints(0).height(10).weight(10).name("pidgeot").build(),
                Pokemon.builder().id(2L).healthPoints(0).height(100).weight(1000).name("raichu").build()
        );
        Mockito.when(this.pokemonRepository.findAll()).thenReturn(results);

        List<PokemonDto> actualListPokemon = this.pokemonService.listPokemon();

        verify(this.pokemonRepository).findAll();
        assertThat(actualListPokemon, hasSize(2));
    }
/*
    @Test
    @DisplayName("Test service to create player with no exist game")
    void test_service_add_player_no_exit_game() {

        Game game = Game.builder().id(1L).name("name").build();
        Player player = Player.builder().game(game).name("player").build();

        Mockito.when(this.gameRepository.findById(1L)).thenThrow(NotFoundException.class);


        assertAll(() -> {
            NotFoundException exception= Assertions.assertThrows(NotFoundException.class,()->this.playerService.createPlayer(
                    PlayerRequest.builder().gameId(1L).name("player").build())
            );
        });
        verify(this.gameRepository).findById(1L);

    }

    @Test
    @DisplayName("Test service to create player with  2 players  game")
    void test_service_add_player_game_with_more_2_player() {

        Player player = Player.builder().game(Game.builder().id(1L).build()).name("player").build();

        Mockito.when(this.gameRepository.findById(1L)).thenReturn(
                Optional.of(Game.builder().id(1L).name("name").build()));
        Mockito.when(this.playerRepository.save(player)).thenThrow(ApplicationException.class);

        assertAll(() -> {
            Assertions.assertThrows(ApplicationException.class,()->this.playerService.createPlayer(
                    PlayerRequest.builder().gameId(1L).name("player").build())
            );
        });
        verify(this.gameRepository).findById(1L);
        verify(this.playerRepository).save(player);

    }

    @Test
    @DisplayName("Test service to list player")
    void test_service_list_player(){

        Mockito.when(this.playerRepository.findAll()).thenReturn(Arrays.asList(Player.builder().id(1L).name("playe1").build(),
                Player.builder().id(1L).name("player2").build()));

        List<PlayerDto> actualListPlayers=this.playerService.listPlayers();

        verify(this.playerRepository).findAll();
        assertThat(actualListPlayers, hasSize(2));


    }*/

}