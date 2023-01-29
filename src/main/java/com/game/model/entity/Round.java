package com.game.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "round_generator")
    @SequenceGenerator(name = "round_generator", sequenceName = "round_seq", allocationSize = 1)
    private Long id;


    @OneToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id",nullable=true)
    private Player winner;


    @ManyToOne
    @JoinColumn(name="battle_id", nullable=false)
    private Battle battle;


}
