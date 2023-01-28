package com.game.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.model.dto.GameDto;
import com.game.model.request.GameRequest;
import com.game.service.GameService;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameController.class)
 class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
     GameService gameService;

    @Autowired
     ObjectMapper objectMapper;

    @Test
    @DisplayName("test add new game")
    void test_add_game() throws Exception {

        GameRequest gameRequest=GameRequest.builder()
                .name("name")
                .build();
        GameDto gameDto= GameDto.builder()
                .id(1L)
                .name("name")
                .build();

        Mockito.when(gameService.createGame(gameRequest)).thenReturn(gameDto);

        mockMvc.perform(post("/game-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(gameRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results.id").value(1L))
                .andExpect(jsonPath("$.results.name").value(gameDto.getName()));
    }

    @Test
    @DisplayName("test get list of game")
    void test_get_list_game() throws Exception {
        List<GameDto> listGames= Arrays.asList(GameDto.builder().id(1L).name("name1").build(),
                GameDto.builder().id(1L).name("name2").build());

        Mockito.when(gameService.listGames()).thenReturn(listGames);

        mockMvc.perform(get("/game-management"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results", hasSize(2)));
    }

}
