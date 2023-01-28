package com.game.service;


import com.game.conf.ApplicationProperties;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.BattleMapper;
import com.game.mapper.BattleMapperImpl;
import com.game.model.dto.BattleDto;
import com.game.model.dto.WinnerDto;
import com.game.model.entity.*;
import com.game.model.request.BattleRequest;
import com.game.repository.BattleRepository;
import com.game.repository.GameRepository;
import com.game.repository.RoundRepository;
import com.game.service.impl.BattleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.game.enums.ApplicationError.DEFAULT_REST_TEMPLATE_ERROR;
import static com.game.enums.ApplicationError.ERROR_BATTLE_PROGRESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    private BattleService battleService;

    @Mock
    private BattleRepository battleRepository;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private RoundRepository roundRepository;
    @Spy
    private BattleMapper battleMapper=new BattleMapperImpl();
    @Spy
    private  ApplicationProperties applicationProperties;



    @BeforeEach
    void setUp() {
        battleService=new BattleServiceImpl(battleRepository,gameRepository,
                roundRepository,battleMapper,applicationProperties) ;
    }

    @Test
    @DisplayName("Test service to create battle")
    void test_service_add_battle() {
        Game game=Game.builder().id(1L).name("name")
                .players(new HashSet<>(Arrays.asList(
                        Player.builder().id(1L).pokemon(Pokemon.builder().build()).build(),
                        Player.builder().id(2L).pokemon(Pokemon.builder().build()).build()
                )))
                .build();
        Battle battle=createBattle();
        List<Round> rounds=new ArrayList<>();
        for(int i=0;i<3;i++)
            rounds.add(Round.builder().id((long)i+1).battle(battle).build());

        Mockito.when(this.gameRepository.findById(1L)).thenReturn(Optional.of(game));
        Mockito.when(this.applicationProperties.getNumberRound()).thenReturn(3);
        Mockito.when(this.battleRepository.save(battle)).thenReturn(Battle.builder().id(1L).name("battle").roundNumber(3)
                .rounds(rounds).game(Game.builder().id(1L).build()).build());

        BattleDto actualBattleDto =this.battleService.createBattle(BattleRequest.builder().gameId(1L)
                .name("battle").build());

        verify(this.gameRepository).findById(1L);
        verify(this.battleRepository).save(battle);
        assertAll(() -> {
            assertEquals(1L, actualBattleDto.getId());
            assertEquals("battle", actualBattleDto.getName());
            assertThat(actualBattleDto.getRounds() ,hasSize(3));
        });
    }

    @Test
    @DisplayName("Test service to create battle with no exist game")
    void test_service_add_battle_with_no_exist_game() {

        BattleRequest battleRequest=BattleRequest.builder().gameId(1L)
                .name("battle").build();

        Mockito.when(this.gameRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertAll(() -> {
            assertThrows(NotFoundException.class,()->this.battleService.createBattle(battleRequest));
        });
        verify(this.gameRepository).findById(1L);
    }

    @Test
    @DisplayName("Test service to create battle with one player no selected pokemon")
    void test_service_add_battle_with_no_selected_pokemon() {
        Game game=Game.builder().id(1L).name("name")
                .players(new HashSet<>(Arrays.asList(
                        Player.builder().id(1L).pokemon(Pokemon.builder().build()).build(),
                        Player.builder().id(2L).build()
                )))
                .build();
        BattleRequest battleRequest=BattleRequest.builder().gameId(1L)
                .name("battle").build();

        Mockito.when(this.gameRepository.findById(1L)).thenReturn(Optional.of(game));

        assertAll(() -> {
            assertThrows(ApplicationException.class,()->this.battleService.createBattle(battleRequest));
        });
        verify(this.gameRepository).findById(1L);
    }

    @Test
    @DisplayName("Test service to create battle with no player ")
    void test_service_add_battle_with_no_player() {
        Game game=Game.builder().id(1L).name("name").build();
        BattleRequest battleRequest=BattleRequest.builder().gameId(1L)
                .name("battle").build();
        Mockito.when(this.gameRepository.findById(1L)).thenReturn(Optional.of(game));

        assertAll(() -> {
            assertThrows(ApplicationException.class,
                    ()->this.battleService.createBattle(battleRequest));
        });
        verify(this.gameRepository).findById(1L);
    }

    @Test
    @DisplayName("Test service to get winner ")
    void test_service_get_winner() {

        Battle battle=createBattle();

        Mockito.when(this.battleRepository.findById(1L)).thenReturn(Optional.of(battle));

        WinnerDto winnerDto=this.battleService.findWinner(1L);

        verify(this.battleRepository).findById(1L);
        assertAll(() -> {
                assertEquals("player1",winnerDto.getPlayerName());
            assertEquals("pidgeot",winnerDto.getPokemonName());
        });

    }

    @Test
    @DisplayName("Test service to get winner with no exist battle ")
    void test_service_get_winner_with_no_exist_battle() {

        Battle battle=createBattle();

        Mockito.when(this.battleRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertAll(() -> {
           assertThrows(NotFoundException.class,()->this.battleService.findWinner(1L));
        });
        verify(this.battleRepository).findById(1L);

    }

    @Test
    @DisplayName("Test service to get winner with battle no terminated")
    void test_service_get_winner_with_battle_no_terminated() {

        Battle battle=createBattleNoTerminated();

        Mockito.when(this.battleRepository.findById(1L)).thenReturn(Optional.of(battle));

        assertAll(() -> {
            ApplicationException exception= assertThrows(ApplicationException.class,()->this.battleService.findWinner(1L));
            assertEquals(ERROR_BATTLE_PROGRESS.getMessage(),exception.getMessage());
        });
        verify(this.battleRepository).findById(1L);

    }



    @Test
        @DisplayName("Test service to list battle")
        void test_service_list_battle(){

            Mockito.when(this.battleRepository.findAll()).thenReturn(
                    Arrays.asList(Battle.builder().id(1L).name("battle1").roundNumber(3).game(Game.builder()
                                    .id(1L).name("name").build()).build(),
                            Battle.builder().id(2L).name("battle2").roundNumber(3).game(Game.builder()
                                    .id(1L).name("name").build()).build()));

            List<BattleDto> actualListBattles=this.battleService.listBattles();

            verify(this.battleRepository).findAll();
            assertThat(actualListBattles, hasSize(2));

    }

    private Battle createBattle() {
        return Battle.builder().name("battle").roundNumber(3)
                .rounds(rounds())
                .game(Game.builder().id(1L).build()).build();
    }
    private Battle createBattleNoTerminated() {
        return Battle.builder().name("battle").roundNumber(3)
                .rounds(Arrays.asList(Round.builder().id(1L).build(),
                        Round.builder().id(2L).winner(Player.builder().build()).build()
                        ,Round.builder().id(3L).build()))
                .game(Game.builder().id(1L).build()).build();
    }
    private List<Round> rounds() {
        Player player=Player.builder().id(1L).name("player1").pokemon(Pokemon.builder().name("pidgeot").build()).build();
        return Arrays.asList(Round.builder().id(1L).winner(player).build(),
                Round.builder().id(2L).winner(player).build()
                ,Round.builder().id(3L).winner(player).build());
    }
}