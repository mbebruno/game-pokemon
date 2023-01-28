package com.game.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BattleRequest {

    private Long battleId;
    @NotBlank(message = "The battle name is empty")
    private  String name;
    @NotNull(message = "The game id is null")
    private Long gameId;
}
