package com.example.padongbe.domain.dongne.service;

import com.example.padongbe.domain.activityMobility.entity.Mobility;
import com.example.padongbe.domain.activityMobility.service.MobilityService;
import com.example.padongbe.domain.dongne.dto.DetailResponse;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.population.service.PopulationService;
import com.example.padongbe.domain.rentPrice.dto.response.RentPriceDto;
import com.example.padongbe.domain.rentPrice.service.RentPriceService;
import com.example.padongbe.domain.safetyGrade.entity.SafetyGrade;
import com.example.padongbe.domain.safetyGrade.service.SafetyGradeService;
import com.example.padongbe.domain.score.service.ScoreCalculator;
import com.example.padongbe.global.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DongneDetailService {

  private final RentPriceService rentPriceService;
  private final MobilityService mobilityService;
  private final ScoreCalculator scoreCalculator;
  private final PopulationService populationService;
  private final DongneService dongneService ;

  public ResponseDTO<DetailResponse> getDongneDetail(String arrivalCode, String departureCode) {
    RentPriceDto[] rentPriceDtos= new RentPriceDto[3];
    rentPriceDtos[0]= rentPriceService.getRentPriceByAdminDongCode(departureCode,"apartment");
    rentPriceDtos[1]= rentPriceService.getRentPriceByAdminDongCode(departureCode,"officetel");
    rentPriceDtos[2]= rentPriceService.getRentPriceByAdminDongCode(departureCode,"villa");
    Mobility mobility= mobilityService.findByGeoCodes(arrivalCode, departureCode);

    double totalMobility= mobility.getTotalMobility();
    double avgTime= mobility.getAvgTime();
    AdminDong adminDong= dongneService.findAdminDongByCode(departureCode);
    SafetyGrade safetyGrade= adminDong.getSafetyGrade();

    double density= populationService.getPopulationByAdmin(adminDong).getDensity();
    double score= scoreCalculator.calculateScore(totalMobility, avgTime,density,
        safetyGrade.getAvgGrade(),rentPriceService.getRentPriceByAdminDongCode(departureCode));

    return ResponseDTO.res(HttpStatus.OK,"상세정보 조회 성공",
    DetailResponse.builder().adminDongCode(departureCode).rentPrice(rentPriceDtos)
        .totalMobility(Math.round(totalMobility*100)/100.0).avgTime(Math.round(avgTime*100)/100.0).score(score).density(Math.round(density*100)/100.0)
        .trafficAccidents(safetyGrade.getTrafficAccidents()).publicSafety(
            safetyGrade.getPublicSafety()).fires(safetyGrade.getFires()).crimes(safetyGrade.getCrimes()).build());

  }

}
