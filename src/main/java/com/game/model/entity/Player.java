package com.game.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_generator")
    @SequenceGenerator(name = "player_generator", sequenceName = "player_seq", allocationSize = 1)
    private Long id;
    private String name;

    @OneToOne
    @JoinColumn(name = "pokemon_id", referencedColumnName = "id",nullable=true)
    private Pokemon pokemon;

    @ManyToOne
    @JoinColumn(name="game_id", nullable=false)
    private Game game;


}
