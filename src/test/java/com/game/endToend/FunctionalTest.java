package com.game.endToend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.model.dto.*;
import com.game.model.request.*;
import com.game.util.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
 class FunctionalTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private GameDto game;
    private PokemonDto pokemon1;
    private PokemonDto pokemon2;
    private PlayerDto player1;
    private PlayerDto player2;


    @BeforeEach
      void setUp() throws Exception {
        loadPokemon();
    }

    @Test
    @DisplayName("Test nominal execution with single Battle")
    void test_single_battle() throws Exception {


        List<PlayerDto> players=initDataOfGame();

        BattleDto battle=createBattle(game.getId(),"My battle");

        attack(battle.getId(),"NORMAL");
        attack(battle.getId(), "SPECIAL");

        WinnerDto winner=getWinner(battle.getId());
        assertTrue(players.stream().anyMatch(item -> winner.getPlayerName().equals(item.getName())));

    }

    @Test
    @DisplayName("Test nominal execution with more Battle")
    void test_more_battle() throws Exception {

        int nbeBattle=0;

        List<PlayerDto> players=initDataOfGame();

        do {

            BattleDto battle = createBattle(game.getId(), "My battle");

            attack(battle.getId(), "SPECIAL");
            attack(battle.getId(), "NORMAL");

            WinnerDto winner = getWinner(battle.getId());
            assertTrue(players.stream().anyMatch(item -> winner.getPlayerName().equals(item.getName())));
            nbeBattle++;
        }while (nbeBattle<2);

    }



    @Test
    @DisplayName("Test nominal execution with Change pokemon")
    void test_battle_change_pokemon() throws Exception {

        int nbeBattle=0;
        List<PlayerDto> players=initDataOfGame();

        do {

            BattleDto battle = createBattle(game.getId(), "My battle");

            attack(battle.getId(), "SPECIAL");
            attack(battle.getId(), "NORMAL");

            WinnerDto winner = getWinner(battle.getId());
            assertTrue(players.stream().anyMatch(item -> winner.getPlayerName().equals(item.getName())));
             pokemon1 = configurePokemen(5L, 40);
             pokemon2 = configurePokemen(10L, 100);
            associetedPlayer(player1.getId(), pokemon1.getId());
            associetedPlayer(player2.getId(), pokemon2.getId());
            nbeBattle++;
        }while (nbeBattle<2);

    }

    private List<PlayerDto> initDataOfGame() throws Exception {
        this.game=createGame("My Game");
         player1 = createPlayer(game.getId(), "player1");
         player2 = createPlayer(game.getId(), "player2");
        PokemonDto pokemon1 = configurePokemen(1L, 10);
        PokemonDto pokemon2 = configurePokemen(2L, 8);
        associetedPlayer(player1.getId(), pokemon1.getId());
        associetedPlayer(player2.getId(), pokemon2.getId());
        return  Arrays.asList(player1, player2);
    }


    private GameDto createGame(String name) throws Exception {
        GameRequest gameRequest=GameRequest.builder()
                .name(name)
                .build();

        MvcResult result=mockMvc.perform(post("/game-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(gameRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();

        return objectMapper.convertValue(objectMapper.readValue(result.getResponse().getContentAsString(), ResponseBody.class)
                .getResults(), GameDto.class);

    }
    private PlayerDto createPlayer(long gameId,String name) throws Exception {

        PlayerRequest request=PlayerRequest.builder()
                .name(name)
                .gameId(gameId)
                .build();

        MvcResult result=mockMvc.perform(post("/player-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();

        return objectMapper.convertValue(objectMapper.readValue(result.getResponse().getContentAsString(), ResponseBody.class)
                .getResults(), PlayerDto.class);

    }
    private  String loadPokemon() throws Exception {
        MvcResult result=mockMvc.perform(get("/pokemon-management/init"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private PokemonDto configurePokemen(long pokemonId, int value) throws Exception {

        PokemonRequest request=PokemonRequest.builder().pokemonId(pokemonId).healthPoints(value).build();

        MvcResult result=mockMvc.perform(patch("/pokemon-management/"+request.getPokemonId()).contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();
        return objectMapper.convertValue(objectMapper.readValue(result.getResponse().getContentAsString(), ResponseBody.class)
                .getResults(), PokemonDto.class);
    }

    private PlayerDto associetedPlayer(long playerId, long pokemonId) throws Exception {
        PlayerPokemonRequest request=PlayerPokemonRequest.builder()
                .pokemonId(pokemonId).playerId(playerId).build();
        MvcResult result=mockMvc.perform(patch("/player-management/"+request.getPlayerId()).contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();
        return objectMapper.convertValue(objectMapper.readValue(result.getResponse().getContentAsString(), ResponseBody.class)
                .getResults(), PlayerDto.class);
    }
    private BattleDto createBattle(Long gameId, String name) throws Exception {

        BattleRequest request=BattleRequest.builder()
                .gameId(gameId)
                .name(name)
                .build();

        MvcResult result=mockMvc.perform(post("/battle-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();
        return objectMapper.convertValue(objectMapper.readValue(result.getResponse().getContentAsString(), ResponseBody.class)
                .getResults(), BattleDto.class);
    }
    private List<RoundDto> attack(Long id, String normal) throws Exception {

        AttackRequest attackRequest=AttackRequest.builder().battleId(id).type(normal).build();

        MvcResult result=mockMvc.perform(patch("/attack-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(attackRequest)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();
        return objectMapper.convertValue(objectMapper.readValue(result.getResponse().getContentAsString(), ResponseBody.class)
                .getResults(), List.class);



    }

    private WinnerDto getWinner(Long id) throws Exception {
        MvcResult result=mockMvc.perform(get("/battle-management/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andReturn();

        return objectMapper.convertValue(objectMapper.readValue(result.getResponse().getContentAsString(), ResponseBody.class)
                .getResults(), WinnerDto.class);
    }


    @Getter
    @Setter
    static class ResponseBody<T> {


        String status;
        Object results;
    }

}
