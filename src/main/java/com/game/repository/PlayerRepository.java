package com.game.repository;


import com.game.model.entity.Game;
import com.game.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player,Long> {

    List<Player> findByGame(Game game);
}
