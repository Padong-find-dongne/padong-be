package com.example.padongbe.domain.population.dto.response;

import com.example.padongbe.domain.population.entity.Population;
import lombok.Getter;

import java.util.List;

@Getter
public class PopulationDataDto {

    private String dongneCode;
    private double density;

    public PopulationDataDto(Population population) {
        this.dongneCode= population.getAdminDong().getAdminDongCode();
        this.density= population.getDensity();
    }

}
