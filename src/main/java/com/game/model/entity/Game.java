package com.game.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_generator")
    @SequenceGenerator(name = "game_generator", sequenceName = "game_seq", allocationSize = 1)
    private Long id;
    private String name;
    @JsonIgnore
    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private Set<Player> players;
    @JsonIgnore
    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private Set<Battle> battles;


}
