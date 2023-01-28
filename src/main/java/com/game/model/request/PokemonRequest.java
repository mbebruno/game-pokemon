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
public class PokemonRequest {

    private Long pokemonId;
    @NotNull(message = "The health points is null")
    private  int healthPoints;

}
