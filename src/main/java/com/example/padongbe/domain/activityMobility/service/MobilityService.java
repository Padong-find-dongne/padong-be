package com.example.padongbe.domain.activityMobility.service;

import static com.example.padongbe.domain.activityMobility.util.ActivityMobilityDataUtill.readActivityMobilityFromExcel;

import com.example.padongbe.domain.activityMobility.dto.MobilityResponse;
import com.example.padongbe.domain.activityMobility.entity.Mobility;
import com.example.padongbe.domain.activityMobility.repository.MobilityRepository;
import com.example.padongbe.global.ResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobilityService {
  private final MobilityRepository mobilityRepository;
  public ResponseDTO<List<MobilityResponse>> searchByDestination(String arrivalCode){
    List<MobilityResponse> responses= new ArrayList<>();
    List<Mobility> mobilities= mobilityRepository.findByArrivalCodeOrderByTotalMobilityDesc(arrivalCode);
    if (mobilities.isEmpty()) return ResponseDTO.res(HttpStatus.NOT_FOUND,"해당 행정동이 존재하지 않습니다.");
    for(Mobility mobility: mobilityRepository.findByArrivalCodeOrderByTotalMobilityDesc(arrivalCode)){
      responses.add(MobilityResponse.builder().departureCode(mobility.getDepartureCode()).totalMobility(mobility.getTotalMobility())
          .avgTime(mobility.getAvgTime()).build());
    }
    return ResponseDTO.res(HttpStatus.OK,"출발 행정동 조회 성공", responses);
  }

  public void fetchData() {
    mobilityRepository.saveAll(readActivityMobilityFromExcel());
  }

}
