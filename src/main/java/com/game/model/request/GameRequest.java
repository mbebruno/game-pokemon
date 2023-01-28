package com.game.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameRequest {


    private Long gameId;
    @NotBlank(message = "The game name is empty")
    private  String name;

}
