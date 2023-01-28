package com.game.service;


import com.game.mapper.GameMapper;
import com.game.mapper.GameMapperImpl;
import com.game.model.dto.GameDto;
import com.game.model.entity.Game;
import com.game.model.request.GameRequest;
import com.game.repository.GameRepository;
import com.game.service.impl.GameServiceImpl;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Spy
    private GameMapper gameMapper=new GameMapperImpl();


    @BeforeEach
    void setUp() {
        gameService=new GameServiceImpl(gameRepository,gameMapper) ;
    }

    @Test
    @DisplayName("Test service to create game")
    void test_service_add_game() {
        Game game = Game.builder().name("name").build();

        Mockito.when(this.gameRepository.save(game)).thenReturn(
                Game.builder().id(1L).name("name").build());

        GameDto actualGameDto = this.gameService.createGame(GameRequest.builder().name("name").build());

        verify(this.gameRepository).save(game);
        assertAll(() -> {
            assertEquals(1L, actualGameDto.getId());
            assertEquals("name", actualGameDto.getName());
        });
    }

        @Test
        @DisplayName("Test service to list game")
        void test_service_list_game(){

            Mockito.when(this.gameRepository.findAll()).thenReturn(Arrays.asList(Game.builder().id(1L).name("name1").build(),
                                                                            Game.builder().id(1L).name("name2").build()));

            List<GameDto> actualListGames=this.gameService.listGames();

            verify(this.gameRepository).findAll();
            assertThat(actualListGames, hasSize(2));


    }
}