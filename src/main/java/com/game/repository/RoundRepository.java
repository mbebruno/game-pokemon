package com.game.repository;

import com.game.model.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository  extends JpaRepository<Round,Long> {
}
