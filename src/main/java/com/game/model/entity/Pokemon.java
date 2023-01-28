package com.game.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pokemon_generator")
    @SequenceGenerator(name = "pokemon_generator", sequenceName = "pokemon_seq", allocationSize = 1)
    private  Long id;
    private  String name;
    private  int height;
    private  int weight;
    private  int healthPoints;
    @JsonBackReference
    @OneToOne(mappedBy = "pokemon")
    private  Player player;


}
