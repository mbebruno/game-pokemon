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
public class PlayerRequest {

    private Long playerId;
    @NotNull(message = "The game id is null")
    private Long gameId;
    @NotBlank(message = "The player name is empty")
    private  String name;


}
