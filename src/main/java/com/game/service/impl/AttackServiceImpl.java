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
        int healthPointsPokemon1=players.get(0).getPokemon().getHealthPoints();
        int healthPointsPokemon2=players.get(1).getPokemon().getHealthPoints();
        return rounds.stream().map(round->{
            Player winner=  battle(players.get(0), players.get(1),
                    finalTypeAttackConfig[1] , finalTypeAttackConfig[2]);
            round.setWinner(winner);
            //re-initialase pokemno with Hp
            players.get(0).getPokemon().setHealthPoints(healthPointsPokemon1);
            players.get(1).getPokemon().setHealthPoints(healthPointsPokemon2);
            this.roundRepository.save(round);
            return this.roundMapper.roundToRoundDto(round);
        }).collect(Collectors.toList());

    }

    /**
     *
     * compute battle between to pokemon
     * @param player1  value of first player
     * @param player2  value of second player
     * @param max  max HP to generate
     * @param min  value HP to generate
     * @return player winner , pokemon with "HealthPoints" more than 0
     */
    private Player battle(Player player1, Player player2,int max , int min){
        do{
            int damagePokomon1=randomConfig.random().nextInt(max-min)+min;
            int damagePokomon2=randomConfig.random().nextInt(max-min)+min;

           //pokemon2  attack pokemeno1
            player1.getPokemon().setHealthPoints(player1.getPokemon().getHealthPoints()-damagePokomon1);
            //pokemon2  attack pokemeno1
            player2.getPokemon().setHealthPoints(player2.getPokemon().getHealthPoints()-damagePokomon2);

        }while(player1.getPokemon().getHealthPoints() >= 1  && player2.getPokemon().getHealthPoints() >=1);

        if(player1.getPokemon().getHealthPoints() < 1)
            return player2;
        else
            return player1;
    }


}
