package com.example.padongbe.domain.population.service;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.service.DongneService;
import com.example.padongbe.domain.population.dto.response.PopulationChartDto;
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
    private final DongneService dongneService;

    public void uploadPopulationData() {
        List<Population> populationList = PopulationDataUtil.readPopulationFromCsv();
        populationRepository.saveAll(populationList);
    }

    public PopulationChartDto getPopulationByDongneCode(String dongneCode) {
        AdminDong adminDong = dongneService.findAdminDongByCode(dongneCode);
        List<Population> populationList = populationRepository.findByAdminDong(adminDong);
        return new PopulationChartDto(populationList);
    }

//    private List<Population> findByDongneCode(String dongneCode) {
//        return populationRepository.findByDongneCode(dongneCode);
//    }
}
