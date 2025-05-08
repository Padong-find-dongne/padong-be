package com.example.padongbe.domain.population.service;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.service.DongneService;
import com.example.padongbe.domain.population.dto.response.PopulationDataDto;
import com.example.padongbe.domain.population.entity.Population;
import com.example.padongbe.domain.population.repository.PopulationRepository;
import com.example.padongbe.domain.population.util.DensityDataUtil;
import com.example.padongbe.domain.population.util.PopulationDataUtil;
import com.example.padongbe.global.ResponseDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopulationService {

    private final PopulationRepository populationRepository;

    public void uploadPopulationData() {
        List<Population> populationList = PopulationDataUtil.readPopulationFromCsv();
        log.info(String.valueOf(populationList.size()));
        populationRepository.saveAll(populationList);
    }

    public PopulationDataDto getPopulationByAdmin(AdminDong adminDong) {
        Optional<Population> optional= populationRepository.findByAdminDong(adminDong);
      return optional.map(PopulationDataDto::new).orElse(null);
    }



    public void uploadDensityData() {
        DensityDataUtil.readDensityFromExcel();
    }

//    private List<Population> findByDongneCode(String dongneCode) {
//        return populationRepository.findByDongneCode(dongneCode);
//    }
}
