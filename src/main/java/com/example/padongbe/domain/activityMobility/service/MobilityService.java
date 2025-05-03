package com.example.padongbe.domain.activityMobility.service;

import static com.example.padongbe.domain.activityMobility.util.ActivityMobilityDataUtil.readActivityMobilityFromExcel;

import com.example.padongbe.domain.activityMobility.dto.MobilityResponse;
import com.example.padongbe.domain.activityMobility.entity.Mobility;
import com.example.padongbe.domain.activityMobility.repository.MobilityRepository;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.service.DongneService;
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
    private final DongneService dongneService;

    public ResponseDTO<List<MobilityResponse>> searchByDestination(String arrivalCode) {
        List<MobilityResponse> responses = new ArrayList<>();
        AdminDong adminDong = dongneService.findAdminDongByCode(arrivalCode);
//        List<Mobility> mobilities = mobilityRepository.findByArrivalCodeOrderByTotalMobilityDesc(arrivalCode);
        List<Mobility> mobilities = mobilityRepository.findByArrivalDongOrderByTotalMobilityDesc(adminDong);
        if (mobilities.isEmpty())
            return ResponseDTO.res(HttpStatus.NOT_FOUND, "해당 행정동이 존재하지 않습니다.");

        for (Mobility mobility :mobilities) {
            responses.add(MobilityResponse.builder()
                    .totalMobility(mobility.getTotalMobility())
                    .avgTime(mobility.getAvgTime())
                    .build());
        }
        return ResponseDTO.res(HttpStatus.OK, "출발 행정동 조회 성공", responses);
    }

    public void fetchData() {
        mobilityRepository.saveAll(readActivityMobilityFromExcel());
    }

}
