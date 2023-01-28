package com.game.mapper;

import com.game.model.dto.AttackDto;
import com.game.model.entity.Attack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface AttackMapper {
    //@Mapping( target = "round.attacks", ignore = true)
    AttackDto attackToAttackDto(Attack attack);
}
