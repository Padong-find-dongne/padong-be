package com.example.padongbe.domain.population.dto.response;

import com.example.padongbe.domain.population.entity.Population;
import lombok.Getter;

import java.util.List;

@Getter
public class PopulationChartDto {
    private String dongneCode;
    private List<PopulationDto> populationList;

    public PopulationChartDto(List<Population> populationList) {
        this.dongneCode = populationList.get(0).getDongneCode();
        this.populationList = populationList.stream()
                .map(PopulationDto::new)
                .toList();
    }


}
