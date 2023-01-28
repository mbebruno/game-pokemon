package com.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.model.dto.PlayerDto;
import com.game.model.dto.PokemonDto;
import com.game.model.request.PlayerPokemonRequest;
import com.game.model.request.PlayerRequest;
import com.game.service.PlayerService;
import com.game.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static com.game.enums.ApplicationError.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PlayerController.class)
class PlayerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PlayerService playerService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Test add new player with correct input data")
    void test_add_player_correct_input() throws Exception {

        PlayerRequest playerRequest=PlayerRequest.builder().gameId(1L).name("player1").build();
        PlayerDto playerDto= PlayerDto.builder().id(1L).name("player1").build();

        Mockito.when(this.playerService.createPlayer(playerRequest)).thenReturn(playerDto);

        mockMvc.perform(post("/player-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(playerRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results.id").value(1L))
                .andExpect(jsonPath("$.results.name").value(playerDto.getName()));
    }

    @Test
    @DisplayName("Test add new player with no exist game id")
    void test_add_player_with_no_exist_game() throws Exception {

        PlayerRequest playerRequest=PlayerRequest.builder().gameId(1L).name("player1").build();
        PlayerDto playerDto= PlayerDto.builder().id(1L).name("player1").build();

        Mockito.when(this.playerService.createPlayer(playerRequest))
                .thenThrow(new NotFoundException(ERROR_FINDING_GAME, playerRequest.getGameId()));

        mockMvc.perform(post("/player-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(playerRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(Message.MSG_FAILED))
                .andExpect(jsonPath("$.message",
                        containsString("A game with Id 1 does not exist !")));

    }

    @Test
    @DisplayName("Test add new player to the game with more 2 players")
    void test_add_player_game_with_more_2_player() throws Exception {

        PlayerRequest playerRequest=PlayerRequest.builder().gameId(1L).name("player").build();
        PlayerDto playerDto= PlayerDto.builder().id(1L).name("player").build();

        Mockito.when(this.playerService.createPlayer(playerRequest))
                .thenThrow(new ApplicationException(ERROR_LIMIT_GAME_PLAYER, playerRequest.getGameId()));

        mockMvc.perform(post("/player-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(playerRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(Message.MSG_FAILED))
                .andExpect(jsonPath("$.message",
                        containsString("A game with id 1 already has 2 players !")));



    }

    @Test
    @DisplayName("Test add pokemon to player")
    void test_add_pokemon_to_player() throws Exception {

        PlayerPokemonRequest playerPokemonRequest=PlayerPokemonRequest.builder()
                .pokemonId(1L).playerId(1L).build();

        PlayerDto playerDto= PlayerDto.builder().id(1L).name("player").
                pokemon(PokemonDto.builder().id(1L).weight(10).height(10).healthPoints(10).name("pidgeot").build())
                .build();

        Mockito.when(this.playerService.addPokemonToPlayer(playerPokemonRequest)).thenReturn(playerDto);

        mockMvc.perform(patch("/player-management/"+playerPokemonRequest.getPlayerId()).contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(playerPokemonRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results.id").value(1L))
                .andExpect(jsonPath("$.results.name").value(playerDto.getName()))
                .andExpect(jsonPath("$.results.pokemon.id").value(playerDto.getPokemon().getId()))
                .andExpect(jsonPath("$.results.pokemon.name").value(playerDto.getPokemon().getName()));

    }

    @Test
    @DisplayName("Test add pokemon to no exist player")
    void test_add_pokemon_no_exist_player() throws Exception {

        PlayerPokemonRequest playerPokemonRequest=PlayerPokemonRequest.builder()
                .pokemonId(1L).playerId(1L).build();


        Mockito.when(this.playerService.addPokemonToPlayer(playerPokemonRequest)).thenThrow(
                new NotFoundException(ERROR_FINDING_PLAYER,playerPokemonRequest.getPlayerId()));

        mockMvc.perform(patch("/player-management/"+playerPokemonRequest.getPlayerId()).contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(playerPokemonRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(Message.MSG_FAILED))
                .andExpect(jsonPath("$.message",
                        containsString("A player with Id 1 does not exist !")));

    }

    @Test
    @DisplayName("Test add no exist pokemon to  player")
    void test_add_no_exist_pokemon_to_player() throws Exception {

        PlayerPokemonRequest playerPokemonRequest=PlayerPokemonRequest.builder()
                .pokemonId(1L).playerId(1L).build();

        Mockito.when(this.playerService.addPokemonToPlayer(playerPokemonRequest)).thenThrow(
                new NotFoundException(ERROR_FINDING_POKEMON,playerPokemonRequest.getPlayerId()));

        mockMvc.perform(patch("/player-management/"+playerPokemonRequest.getPlayerId()).contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(playerPokemonRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(Message.MSG_FAILED))
                .andExpect(jsonPath("$.message",
                        containsString("A pokemon with Id 1 does not exist !")));

    }

    @Test
    @DisplayName("Test get list of players")
    void test_get_list_players() throws Exception {

        Mockito.when(this.playerService.listPlayers()).thenReturn
                (Arrays.asList(PlayerDto.builder().id(1L).name("player1").build(),
                PlayerDto.builder().id(1L).name("player2").build()));

        mockMvc.perform(get("/player-management"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results", hasSize(2)));
    }


}