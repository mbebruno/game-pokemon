package com.game.service.impl;

import com.game.conf.ApplicationProperties;
import com.game.enums.ApplicationError;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.BattleMapper;
import com.game.model.dto.BattleDto;
import com.game.model.dto.PlayerDto;
import com.game.model.dto.WinnerDto;
import com.game.model.entity.Battle;
import com.game.model.entity.Game;
import com.game.model.entity.Player;
import com.game.model.entity.Round;
import com.game.model.request.BattleRequest;
import com.game.repository.BattleRepository;
import com.game.repository.GameRepository;
import com.game.repository.RoundRepository;
import com.game.service.BattleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.game.enums.ApplicationError.*;

@AllArgsConstructor
@Service
public class BattleServiceImpl implements BattleService {

    private final BattleRepository battleRepository;
    private final GameRepository gameRepository;
    private final RoundRepository roundRepository;
    private final BattleMapper battleMapper;


    private final ApplicationProperties applicationProperties;


    /**
     *
     * create battle and automatic create rounds ,players must be created and players must be have pokemon
     * @param battleRequest  data contain game ID and name of battle
     * @return data attack applied to other pokemon
     */

    @Override
    @Transactional
    public BattleDto createBattle(BattleRequest battleRequest) {

        //check if game exist
        Game game=this.gameRepository.findById(battleRequest.getGameId())
                .orElseThrow(()-> new NotFoundException(ERROR_FINDING_GAME, battleRequest.getGameId()));

        //check if game have 2 players
        if(game.getPlayers()==null || game.getPlayers().size()<2)
            throw new ApplicationException(ERROR_ADD_GAME_PLAYER,battleRequest.getGameId());

        //check if player choice pokemon
        if(!game.getPlayers().stream().allMatch(player->player.getPokemon()!=null))
            throw new ApplicationException(ERROR_SELECT_POKEMON,battleRequest.getGameId());
        //Save battle
        Battle battle=battleMapper.battleRequestToBattle(battleRequest);
        battle.setRoundNumber(applicationProperties.getNumberRound());

        //Create rounds
        List<Round> rounds=new ArrayList<>();
        for(int i=0;i<applicationProperties.getNumberRound();i++)
            rounds.add(Round.builder().battle(battle).build());
        battle.setRounds(rounds);

        return this.battleMapper.battleToBattleDto(this.battleRepository.save(battle));
    }

    /**
     *
     * find winner player of battle
     * @return winner player and pokemon
     */
    public WinnerDto findWinner(long battleId) {

        //check if ballte exist
        Battle battle=this.battleRepository.findById(battleId)
                .orElseThrow(()-> new NotFoundException(ERROR_FINDING_BATTLE, battleId));

        if(battle.getRounds().stream().allMatch(item->item.getWinner()!=null)) {
            //group by round win
            Player playerWinner = battle.getRounds().stream().filter(round -> round.getWinner() != null).collect(
                            Collectors.groupingBy(Round::getWinner, Collectors.counting()))
                    //find number max win
                            .entrySet().stream().max((key, val) -> key.getValue().intValue()).map(Map.Entry::getKey)
                                .orElseThrow(() -> new ApplicationException(ApplicationError.DEFAULT_REST_TEMPLATE_ERROR));

            return new WinnerDto(playerWinner.getName(),playerWinner.getPokemon().getName());

        }
        throw new ApplicationException(ApplicationError.ERROR_BATTLE_PROGRESS);

    }

    /**
     *
     * list all battles
     * @return list of battles
     */
    @Override
    public List<BattleDto> listBattles() {
        return this.battleRepository.findAll().stream()
                .map(battleMapper::battleToBattleDto)
                .collect(Collectors.toList());
    }
}
