package com.game.model.request;

import com.game.enums.TypeAttack;
import com.game.model.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttackRequest {

    @NotNull(message = "The battle id is null")
    private Long battleId;
    private String type;

}
