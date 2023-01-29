package com.game.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "battle_generator")
    @SequenceGenerator(name = "battle_generator", sequenceName = "battle_seq", allocationSize = 1)
    private Long id;
    private String name;
    private int roundNumber;


    @ManyToOne
    @JoinColumn(name="game_id", nullable=false)
    private Game game;
    @JsonIgnore
    @OneToMany(
            mappedBy = "battle",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Round> rounds;
}
