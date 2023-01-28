package com.game.model.entity;

import lombok.*;

import javax.persistence.*;

@Setter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Attack {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attack_generator")
    @SequenceGenerator(name = "attack_generator", sequenceName = "attack_seq", allocationSize = 1)
    private Long id;

    private int healthPointsDamage;
    private String type;

}
