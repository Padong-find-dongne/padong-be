package com.example.padongbe.domain.population.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Population {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dongneCode;
    private String gender; // "계", "남", "여"
    private double age0to9;
    private double age10to19;
    private double age20to29;
    private double age30to39;
    private double age40to49;
    private double age50to59;
    private double age60plus;
}
