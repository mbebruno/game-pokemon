package com.game.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerPokemonRequest {


    private Long playerId;
    @NotNull(message = "The pokemon id is null")
    private Long pokemonId;

}
