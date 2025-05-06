package com.example.padongbe.domain.activityMobility.service;

import static com.example.padongbe.domain.activityMobility.util.ActivityMobilityDataUtil.readActivityMobilityFromExcel;

import com.example.padongbe.domain.activityMobility.dto.MobilityResponse;
import com.example.padongbe.domain.activityMobility.entity.Mobility;
import com.example.padongbe.domain.activityMobility.repository.MobilityRepository;
import com.example.padongbe.domain.dongne.dto.AdminDongDto;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.service.DongneService;
import com.example.padongbe.global.ResponseDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public ResponseDTO<List<MobilityResponse>> searchByAddress(String address) {
        AdminDong adminDong = dongneService.findAdminDongByAddress(address);
        List<MobilityResponse> responses = new ArrayList<>();
        List<Mobility> mobilities = mobilityRepository.findByArrivalDongOrderByTotalMobilityDesc(adminDong);
        if (mobilities.isEmpty())
            return ResponseDTO.res(HttpStatus.NOT_FOUND, "해당 행정동이 존재하지 않습니다.");

        for (Mobility mobility :mobilities) {
            AdminDong departureDong = mobility.getDepartureDong();
            responses.add(MobilityResponse.builder()
                    .departureDong(AdminDongDto.builder()
                                    .adminDongCode(departureDong.getAdminDongCode())
                                    .address(departureDong.getCity()+" "+departureDong.getDistrict()+" "+departureDong.getAdminAreaName())
                                    .build())
                    .totalMobility(mobility.getTotalMobility())
                    .avgTime(mobility.getAvgTime())
                    .build());
        }

        return ResponseDTO.res(HttpStatus.OK, "출발 행정동 조회 성공", responses);
    }

    public ResponseDTO<List<MobilityResponse>> searchByTwoAddresses(String address1, String address2) {
        List<MobilityResponse> response1 = searchByAddress(address1).getData();
        List<MobilityResponse> response2 = searchByAddress(address2).getData();

        Map<String, MobilityResponse> response1Map = response1.stream()
                .collect(Collectors.toMap(
                        r -> r.getDepartureDong().getAdminDongCode(),
                        Function.identity(),
                        (a, b) -> a
                ));

        List<MobilityResponse> result = new ArrayList<>();

        for (MobilityResponse r2 : response2) {
            String code = r2.getDepartureDong().getAdminDongCode();
            MobilityResponse r1 = response1Map.get(code);
            if (r1 != null) {
                MobilityResponse mergedResponse = MobilityResponse.builder()
                        .departureDong(r1.getDepartureDong())
                        .totalMobility(r1.getTotalMobility() + r2.getTotalMobility())
                        .avgTime((r1.getAvgTime() + r2.getAvgTime()) / 2)
                        .build();
                result.add(mergedResponse);
            }
        }

        List<MobilityResponse> finalResult;
        if (result.isEmpty()) {
            finalResult = new ArrayList<>();
            finalResult.addAll(response1);
            finalResult.addAll(response2);
        } else {
            finalResult = result;
        }
        finalResult.sort(Comparator.comparingDouble(MobilityResponse::getAvgTime));
        return ResponseDTO.res(HttpStatus.OK, "출발 행정동 조회 성공", finalResult);
    }

}
