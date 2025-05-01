package com.example.padongbe.domain.population.service;

import com.example.padongbe.domain.population.dto.PopulationChartDto;
import com.example.padongbe.domain.population.dto.PopulationDto;
import com.example.padongbe.domain.population.entity.Population;
import com.example.padongbe.domain.population.repository.PopulationRepository;
import com.example.padongbe.domain.population.util.PopulationDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopulationService {

    private final PopulationRepository populationRepository;

    public void uploadPopulationData() {
        List<Population> populationList = PopulationDataUtil.readPopulationFromCsv();
        populationRepository.saveAll(populationList);
    }

    public PopulationChartDto getPopulationByDongneCode(String dongneCode) {
        List<Population> populationList = findByDongneCode(dongneCode);
        return new PopulationChartDto(populationList);
    }

    private List<Population> findByDongneCode(String dongneCode) {
        return populationRepository.findByDongneCode(dongneCode);
    }
}
