package com.game.service;

import com.game.model.dto.AttackDto;
import com.game.model.dto.RoundDto;
import com.game.model.entity.Pokemon;
import com.game.model.request.AttackRequest;

import java.util.List;

public interface AttackService {

    List<RoundDto> launchAttack(AttackRequest attackRequest);

}
