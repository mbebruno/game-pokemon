package com.game.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "game.pokemon")
public class ApplicationProperties {

    private String pokeapiList;
    private int numberRound;
    private int[] typeAttackNormal;
    private int[] typeAttackSpecial;

}
