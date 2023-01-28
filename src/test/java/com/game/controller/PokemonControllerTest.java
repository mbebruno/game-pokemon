package com.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.model.dto.PokemonDto;
import com.game.model.request.PokemonRequest;
import com.game.service.PokemonService;
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

import static com.game.enums.ApplicationError.ERROR_FINDING_POKEMON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PokemonController.class)
class PokemonControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PokemonService pokemonService;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("Test configure  Pokemon HP with correct input data")
    void test_configure_pokemon_correct_input() throws Exception {

        PokemonRequest pokemonRequest=PokemonRequest.builder().pokemonId(1L).healthPoints(10).build();
        PokemonDto pokemonDto= PokemonDto.builder().id(1L).healthPoints(10).height(10).weight(10).name("pidgeot").build();

        Mockito.when(this.pokemonService.configureHealthPoints(pokemonRequest,pokemonRequest.getPokemonId())).thenReturn(pokemonDto);

        mockMvc.perform(patch("/pokemon-management/"+pokemonRequest.getPokemonId()).contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(pokemonRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results.id").value(1L))
                .andExpect(jsonPath("$.results.name").value(pokemonDto.getName()))
                .andExpect(jsonPath("$.results.healthPoints").value(pokemonDto.getHealthPoints()))
                .andExpect(jsonPath("$.results.height").value(pokemonDto.getHeight()))
                .andExpect(jsonPath("$.results.weight").value(pokemonDto.getWeight()));
    }

    @Test
    @DisplayName("Test configure HP with  with no exist pokemon id")
    void test_configure_hp_with_no_exist_pokemon() throws Exception {

        PokemonRequest pokemonRequest=PokemonRequest.builder()
                .pokemonId(1L).healthPoints(10).build();

        Mockito.when(this.pokemonService.configureHealthPoints(pokemonRequest,pokemonRequest.getPokemonId()))
                .thenThrow(new NotFoundException(ERROR_FINDING_POKEMON,pokemonRequest.getPokemonId()));

        mockMvc.perform(patch("/pokemon-management/"+pokemonRequest.getPokemonId()).contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(pokemonRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(Message.MSG_FAILED))
                .andExpect(jsonPath("$.message",
                        containsString("A pokemon with Id 1 does not exist !")));

    }


    @Test
    @DisplayName("Test get list of Pokemon")
    void test_get_list_pokemon() throws Exception {

        Mockito.when(this.pokemonService.listPokemon()).thenReturn(Arrays.asList(
                PokemonDto.builder().id(1L).healthPoints(0).height(10).weight(10).name("pidgeot").build(),
                PokemonDto.builder().id(2L).healthPoints(0).height(100).weight(1000).name("raichu").build()));

        mockMvc.perform(get("/pokemon-management"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results", hasSize(2)));
    }


    @Test
    @DisplayName("Test load pokemon from remote api")
    void test_load_pokemon() throws Exception {

        Mockito.when(this.pokemonService.loadPokemonFromRemoteApi()).thenReturn(Message.MSG_INITIALISE);

        mockMvc.perform(get("/pokemon-management/init"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES));

    }

    @Test
    @DisplayName("Test load pokemon from remote api with binding exception")
    void test_load_pokemon_with_exception() throws Exception {

        Mockito.when(this.pokemonService.loadPokemonFromRemoteApi()).thenThrow(ApplicationException.class);

        mockMvc.perform(get("/pokemon-management/init"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(Message.MSG_FAILED));
    }




}