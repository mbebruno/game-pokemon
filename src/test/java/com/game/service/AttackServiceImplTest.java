package com.game.service;

import com.game.conf.ApplicationProperties;
import com.game.conf.RandomConfig;
import com.game.enums.TypeAttack;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.RoundMapper;
import com.game.mapper.RoundMapperImpl;
import com.game.model.dto.RoundDto;
import com.game.model.entity.*;
import com.game.model.request.AttackRequest;
import com.game.repository.BattleRepository;
import com.game.repository.PlayerRepository;
import com.game.repository.RoundRepository;
import com.game.service.impl.AttackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttackServiceImplTest {

    private AttackService attackService;

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private  BattleRepository battleRepository;
    @Mock
    private  RoundRepository roundRepository;
    @Spy
    private  RandomConfig randomConfig;

    @Spy
    private  RoundMapper roundMapper=new RoundMapperImpl();
    @Spy
    private  ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        attackService=new AttackServiceImpl(playerRepository,battleRepository,
                roundRepository,randomConfig,roundMapper,applicationProperties) ;
    }

    @Test
    @DisplayName("Test service to launch normal attack")
    void test_service_launch_normal_attack() {

        int[] typeAttackConfig={1,10,1};
        Battle battle=createBattle();
        AttackRequest attackRequest= AttackRequest.builder().battleId(1L).type("NORMAL").build();

        ArgumentCaptor<Round> acRound = ArgumentCaptor.forClass(Round.class);

        when(this.applicationProperties.getTypeAttackNormal()).thenReturn(typeAttackConfig);
        when(this.battleRepository.findById(1L)).thenReturn(Optional.of(battle));
        when(this.playerRepository.findByGame(battle.getGame())).thenReturn(createListePlayers());
        when(this.roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        List<RoundDto> actualRoundDtos =this.attackService.launchAttack(attackRequest);

        verify(this.battleRepository).findById(1L);
        verify(this.playerRepository).findByGame(battle.getGame());
        verify(this.roundRepository,times(1)).save(acRound.capture());
        assertAll(() -> {
            assertThat(acRound.getAllValues() ,hasSize(1));
            assertEquals(1L,acRound.getAllValues().get(0).getId());
            assertThat(actualRoundDtos ,hasSize(1));
            assertEquals(1L, actualRoundDtos.get(0).getId());

        });
    }

    @Test
    @DisplayName("Test service to launch special attack")
    void test_service_launch_special_attack() {

        int[] typeAttackConfig={2,15,2};
        Battle battle=createBattle();
        AttackRequest attackRequest= AttackRequest.builder().battleId(1L).type("SPECIAL").build();

        ArgumentCaptor<Round> acRound = ArgumentCaptor.forClass(Round.class);

        when(this.applicationProperties.getTypeAttackSpecial()).thenReturn(typeAttackConfig);
        when(this.battleRepository.findById(1L)).thenReturn(Optional.of(battle));
        when(this.playerRepository.findByGame(battle.getGame())).thenReturn(createListePlayers());
        when(this.roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        List<RoundDto> actualRoundDtos =this.attackService.launchAttack(attackRequest);

        verify(this.battleRepository).findById(1L);
        verify(this.playerRepository).findByGame(battle.getGame());
        verify(this.roundRepository,times(2)).save(acRound.capture());
        assertAll(() -> {
            assertThat(acRound.getAllValues() ,hasSize(2));
            assertThat(actualRoundDtos ,hasSize(2));
        });
    }

    @Test
    @DisplayName("Test service to launch  attack with no exist battle")
    void test_service_launch_attack_no_exist_battle() {

        int[] typeAttackConfig={2,15,2};
        AttackRequest attackRequest= AttackRequest.builder().battleId(1L).type("NORMAL").build();

        when(this.applicationProperties.getTypeAttackNormal()).thenReturn(typeAttackConfig);
        when(this.battleRepository.findById(1L)).thenThrow(NotFoundException.class);

        assertAll(() -> {
            assertThrows(NotFoundException.class,()->this.attackService.launchAttack(attackRequest));
        });
        verify(this.battleRepository).findById(1L);
    }
    @Test
    @DisplayName("Test service to launch  attack with round terminated")
    void test_service_launch_attack_round_terminated() {

        int[] typeAttackConfig={2,15,2};
        Battle battle=createBattleTerminated();
        AttackRequest attackRequest= AttackRequest.builder().battleId(1L).type("NORMAL").build();

        when(this.applicationProperties.getTypeAttackNormal()).thenReturn(typeAttackConfig);
        when(this.battleRepository.findById(1L)).thenReturn(Optional.of(battle));

        assertAll(() -> {
            assertThrows(ApplicationException.class,()->this.attackService.launchAttack(attackRequest));
        });
        verify(this.battleRepository).findById(1L);
    }

    /**
     * Data
     */

    private List<Round> rounds() {
        Player player=Player.builder().id(1L).name("player1").build();
        return Arrays.asList(Round.builder().id(1L).build(),
                Round.builder().id(2L).build()
                ,Round.builder().id(3L).winner(player).build());
    }
    private List<Round> roundsTerminated() {
        Player player=Player.builder().id(1L).name("player1").build();
        return Arrays.asList(Round.builder().winner(player).id(1L).build(),
                Round.builder().id(2L).winner(player).build()
                ,Round.builder().id(3L).winner(player).build());
    }
    private List<Player> createListePlayers() {
        //Pokemon pokemon=Pokemon.builder().healthPoints(10).build();
        return Arrays.asList(Player.builder().id(1L).name("playe1").pokemon(Pokemon.builder().healthPoints(10).build()).build(),
                Player.builder().id(2L).name("player2").pokemon(Pokemon.builder().healthPoints(20).build()).build());
    }

    private Battle createBattle() {

        return Battle.builder().id(1L).game(creatGame()).rounds(rounds()).build();
    }
    private Battle createBattleTerminated() {

        return Battle.builder().id(1L).game(creatGame()).rounds(roundsTerminated()).build();
    }

    private Game creatGame() {

        return Game.builder().id(1L).name("name").build();
    }


}