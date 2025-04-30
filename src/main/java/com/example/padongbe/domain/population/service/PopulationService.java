package com.example.padongbe.domain.population.service;

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
}
