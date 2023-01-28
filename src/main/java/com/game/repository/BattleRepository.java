package com.game.repository;

import com.game.model.entity.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRepository  extends JpaRepository<Battle,Long> {
}
