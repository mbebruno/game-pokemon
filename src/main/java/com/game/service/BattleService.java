package com.game.service;

import com.game.model.dto.BattleDto;
import com.game.model.dto.PlayerDto;
import com.game.model.dto.WinnerDto;
import com.game.model.request.BattleRequest;

import java.util.List;

public interface BattleService {

    BattleDto createBattle(BattleRequest battleRequest);

    List<BattleDto> listBattles();

    WinnerDto findWinner(long id);
}
