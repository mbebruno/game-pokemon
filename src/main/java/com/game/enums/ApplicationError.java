package com.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationError {


    DEFAULT_REST_TEMPLATE_ERROR("Somethings wrong when call a api with rest template"),

    POKEMON_REST_BIND_ERROR("Somethings wrong when bind data from  pokemon details remote api : %s"),

    ERROR_FINDING_GAME("A game with Id %s does not exist !"),
    ERROR_LIMIT_GAME_PLAYER("A game with id %s already has 2 players !"),
    ERROR_ADD_GAME_PLAYER("A game with id %s no have 2 players !"),
    ERROR_FINDING_PLAYER("A player with Id %s does not exist !"),
    ERROR_FINDING_OTHER_PLAYER("Second player no found !"),
    ERROR_FINDING_POKEMON("A pokemon with Id %s does not exist !"),
    ERROR_SELECT_POKEMON("aLL player no selected pokemon for game %s"),
    ERROR_FINDING_BATTLE("A battle with Id %s does not exist !"),
    ERROR_RETRY_GAME("This battle is already over"),
    ERROR_BATTLE_PROGRESS("The battle no terminate"),

    ;

    private final String message;
}
