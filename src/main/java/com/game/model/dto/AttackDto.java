package com.game.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.game.model.entity.Player;
import com.game.model.entity.Round;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttackDto {

    private Long id;
    private PlayerDto player;
    private int healthPointsDamage;
    private RoundDto round;
    private String type;
}
