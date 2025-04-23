package com.example.padongbe.domain.safetyGrade.entity;

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
public class SafetyGrade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String districtName;
    private Long trafficAccidentGrade;
    private Long fireGrade;
    private Long crimeGrade;
    private Long livingGrade;
    private Long suicideGrade;
    private Long epidemicGrade;
}
