package com.game.service.impl;

import com.game.mapper.GameMapper;
import com.game.model.dto.GameDto;
import com.game.model.request.GameRequest;
import com.game.repository.GameRepository;
import com.game.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public GameDto createGame( final GameRequest gameRequest){
        return this.gameMapper.gameToGameDto(this.gameRepository.save(gameMapper.gameRequestToGame(gameRequest)));
    }

    @Override
    public List<GameDto> listGames(){
        return this.gameRepository.findAll().stream()
                .map(gameMapper::gameToGameDto)
                .collect(Collectors.toList());
    }
}
