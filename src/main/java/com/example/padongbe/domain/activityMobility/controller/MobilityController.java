package com.example.padongbe.domain.activityMobility.controller;

import static com.example.padongbe.domain.activityMobility.util.ActivityMobilityDataUtill.readActivityMobilityFromExcel;

import com.example.padongbe.domain.activityMobility.dto.MobilityResponse;
import com.example.padongbe.domain.activityMobility.service.MobilityService;
import com.example.padongbe.global.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobility")
@RequiredArgsConstructor
@Tag(name = "Mobility", description = "서울시 생활이동 데이터 관리 API")
public class MobilityController {
  private final MobilityService mobilityService;
  @GetMapping("/{arrivalCode}")
  public ResponseEntity<ResponseDTO<List<MobilityResponse>>> searchByDestination(@PathVariable String arrivalCode){
    return ResponseEntity.ok(mobilityService.searchByDestination(arrivalCode));
  }

  @GetMapping("/data")
  public void fetchData(){
    mobilityService.fetchData();
  }

}
