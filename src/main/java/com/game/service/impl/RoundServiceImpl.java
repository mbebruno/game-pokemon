package com.game.service.impl;

import com.game.mapper.RoundMapper;
import com.game.model.dto.RoundDto;
import com.game.repository.RoundRepository;
import com.game.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoundServiceImpl implements RoundService {
    private final RoundRepository roundRepository;
    private final RoundMapper roundMapper;


    @Override
    public List<RoundDto> listRounds() {
        return this.roundRepository.findAll().stream()
                .map(this.roundMapper::roundToRoundDto)
                .collect(Collectors.toList());
    }
}
