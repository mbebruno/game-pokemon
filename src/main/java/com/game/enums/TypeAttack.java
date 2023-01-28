package com.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeAttack {
    NORMAL("NORMAL"),
    SPECIAL("SPECIAL"),
    ;
    private final String message;
}
