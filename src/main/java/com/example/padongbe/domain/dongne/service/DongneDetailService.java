package com.example.padongbe.domain.dongne.service;

import com.example.padongbe.domain.activityMobility.entity.Mobility;
import com.example.padongbe.domain.activityMobility.service.MobilityService;
import com.example.padongbe.domain.dongne.dto.DetailResponse;
import com.example.padongbe.domain.dongne.dto.LocationResponse;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.population.service.PopulationService;
import com.example.padongbe.domain.rentPrice.dto.response.RentPriceDto;
import com.example.padongbe.domain.rentPrice.service.RentPriceService;
import com.example.padongbe.domain.safetyGrade.entity.SafetyGrade;
import com.example.padongbe.domain.safetyGrade.service.SafetyGradeService;
import com.example.padongbe.domain.score.service.ScoreCalculator;
import com.example.padongbe.global.ResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class DongneDetailService {

  private final RentPriceService rentPriceService;
  private final MobilityService mobilityService;
  private final ScoreCalculator scoreCalculator;
  private final PopulationService populationService;
  private final DongneService dongneService ;

  private final WebClient googleClient= WebClient.builder()
      .baseUrl("https://maps.googleapis.com").build();
  private final String googleServiceKey= "AIzaSyB3ilG_t89koN7_8vQjBlUwmx64yufBpno";

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
            safetyGrade.getPublicSafety()).fires(safetyGrade.getFires()).crimes(safetyGrade.getCrimes())
        .location(getLocation(adminDong.getDistrict()+" "+adminDong.getAdminAreaName()))
        .build());

  }

  public LocationResponse getLocation(String address) {
    JsonNode json = googleClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/maps/api/geocode/json")
            .queryParam("address", address)
            .queryParam("key", googleServiceKey)
            .build())
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();

    JsonNode location = json.path("results").path(0).path("geometry").path("location");
    double lat = location.path("lat").asDouble();
    double lng = location.path("lng").asDouble();

    LocationResponse response= new LocationResponse(address,lat,lng);

    return response;

  }

}
