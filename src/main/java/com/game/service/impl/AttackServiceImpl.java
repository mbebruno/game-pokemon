package com.game.service.impl;

import com.game.conf.ApplicationProperties;
import com.game.conf.RandomConfig;
import com.game.enums.ApplicationError;
import com.game.exception.ApplicationException;
import com.game.exception.NotFoundException;
import com.game.mapper.RoundMapper;
import com.game.model.dto.RoundDto;
import com.game.model.entity.Battle;
import com.game.model.entity.Player;
import com.game.model.entity.Pokemon;
import com.game.model.entity.Round;
import com.game.model.request.AttackRequest;
import com.game.repository.BattleRepository;
import com.game.repository.PlayerRepository;
import com.game.repository.RoundRepository;
import com.game.service.AttackService;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AttackServiceImpl implements AttackService {


    private final PlayerRepository playerRepository;
    private final BattleRepository battleRepository;
    private final RoundRepository roundRepository;
    private final RandomConfig randomConfig;
     private final RoundMapper roundMapper;
    private final ApplicationProperties applicationProperties;

    /**
     *
     * launch attack from player (input player id) to select other player of game
     * @param attackRequest  data contain roundId and playerId who does attack
     * @return data attack applied to other pokemon
     */
    @Override
    @Transactional
    public List<RoundDto> launchAttack(AttackRequest attackRequest) {

        //check type attack
        int[] typeAttackConfig=applicationProperties.getTypeAttackNormal();  //this variable to manage type  of attack 0=(1=default , 2=special) 1=max and 2=min
         if(Message.TYPE_ATTACK.equals(attackRequest.getType())) {
            typeAttackConfig=applicationProperties.getTypeAttackSpecial();
        }
        //check if battle exist
        Battle battle=this.battleRepository.findById(attackRequest.getBattleId()).orElseThrow(
                ()->new NotFoundException(ApplicationError.ERROR_FINDING_BATTLE,attackRequest.getBattleId()));

        //choice round available
        List<Round> rounds=battle.getRounds().stream().filter(round->round.getWinner()==null).
                limit(typeAttackConfig[0]).collect(Collectors.toList());

        if(rounds.isEmpty())
            throw new ApplicationException(ApplicationError.ERROR_RETRY_GAME);

        //get players of game
        List<Player> players =playerRepository.findByGame(battle.getGame());

        //apply attack to each round
        int[] finalTypeAttackConfig = typeAttackConfig;
        return rounds.stream().map(round->{
            Pokemon winnerRound=  battle(players.get(0).getPokemon(), players.get(1).getPokemon(), finalTypeAttackConfig[1] , finalTypeAttackConfig[2]);
            round.setWinner(winnerRound.getPlayer());
            this.roundRepository.save(round);
            return this.roundMapper.roundToRoundDto(round);
        }).collect(Collectors.toList());

    }

    /**
     *
     * compute battle between to pokemon
     * @param pokemon1  value of first pokemon
     * @param pokemon2  value of second pokemon
     * @param max  max HP to generate
     * @param min  value HP to generate
     * @return Pokemon winner , pokemon with "HealthPoints" more than 0
     */
    private Pokemon battle(Pokemon pokemon1, Pokemon pokemon2,int max , int min){
        do{

           //pokemon2  attack pokemeno1
            pokemon1.setHealthPoints(pokemon1.getHealthPoints()-randomConfig.random().nextInt(max-min)+min);
            //pokemon2  attack pokemeno1
            pokemon2.setHealthPoints(pokemon2.getHealthPoints()-randomConfig.random().nextInt(max-min)+min);

        }while(pokemon1.getHealthPoints() >= 1  && pokemon2.getHealthPoints() >=1);

        if(pokemon1.getHealthPoints() < 1)
            return pokemon2;
        else
            return pokemon1;
    }


}
