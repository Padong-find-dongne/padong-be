package com.example.padongbe.domain.population.dto.response;

import com.example.padongbe.domain.population.entity.Population;
import lombok.Getter;

import java.util.List;

@Getter
public class PopulationDto {
    private String gender;
    private String[] labels = {"0~9세", "10~19세", "20~29세", "30~39세", "40~49세", "50~59세", "60세 이상"};
    private List<Double> data;

    public PopulationDto(Population population) {
        this.gender = population.getGender();
        this.data = List.of(
                population.getAge0to9(),
                population.getAge10to19(),
                population.getAge20to29(),
                population.getAge30to39(),
                population.getAge40to49(),
                population.getAge50to59(),
                population.getAge60plus()
        );
    }

}
