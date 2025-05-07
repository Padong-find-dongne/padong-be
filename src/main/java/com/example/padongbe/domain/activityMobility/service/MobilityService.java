package com.example.padongbe.domain.activityMobility.service;

import com.example.padongbe.domain.activityMobility.dto.IntersectedMobilityResponse;
import com.example.padongbe.domain.activityMobility.dto.MobilityResponse;
import com.example.padongbe.domain.activityMobility.dto.MultiMobilityResponse;
import com.example.padongbe.domain.activityMobility.entity.Mobility;
import com.example.padongbe.domain.activityMobility.repository.MobilityRepository;
import com.example.padongbe.domain.dongne.dto.AdminDongDto;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.service.DongneService;
import com.example.padongbe.global.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.padongbe.domain.activityMobility.util.ActivityMobilityDataUtil.readActivityMobilityFromExcel;

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

    public ResponseDTO<MultiMobilityResponse> searchByTwoAddresses(String address1, String address2) {
        List<MobilityResponse> response1 = searchByAddress(address1).getData();
        List<MobilityResponse> response2 = searchByAddress(address2).getData();

        Map<String, MobilityResponse> map1 = response1.stream()
                .collect(Collectors.toMap(
                        r -> r.getDepartureDong().getAdminDongCode(),
                        Function.identity(),
                        (a, b) -> a
                ));

        Map<String, MobilityResponse> map2 = response2.stream()
                .collect(Collectors.toMap(
                        r -> r.getDepartureDong().getAdminDongCode(),
                        Function.identity(),
                        (a, b) -> a
                ));

        List<IntersectedMobilityResponse> intersected = new ArrayList<>();
        List<MobilityResponse> onlyFromFirst = new ArrayList<>();
        List<MobilityResponse> onlyFromSecond = new ArrayList<>();

        for (Map.Entry<String, MobilityResponse> entry : map1.entrySet()) {
            String code = entry.getKey();
            MobilityResponse r1 = entry.getValue();

            if (map2.containsKey(code)) {
                MobilityResponse r2 = map2.get(code);

                IntersectedMobilityResponse merged = IntersectedMobilityResponse.builder()
                        .departureDong(r1.getDepartureDong())
                        .totalMobility1(r1.getTotalMobility())
                        .totalMobility2(r2.getTotalMobility())
//                        .totalMobility(r1.getTotalMobility() + r2.getTotalMobility())
                        .avgTime1(r1.getAvgTime())
                        .avgTime2(r2.getAvgTime())
                        .build();

                intersected.add(merged);
            } else {
                onlyFromFirst.add(r1);
            }
        }

        for (Map.Entry<String, MobilityResponse> entry : map2.entrySet()) {
            if (!map1.containsKey(entry.getKey())) {
                onlyFromSecond.add(entry.getValue());
            }
        }

        // 1. 필터링
        intersected = intersected.stream()
                .filter(r ->
                        r.getTotalMobility1() >= 10 &&
                                r.getTotalMobility2() >= 10 &&
                                (r.getTotalMobility1() + r.getTotalMobility2() >= 50) &&
                                r.getAvgTime1() >= 20 &&
                                r.getAvgTime2() >= 20
                )
                .collect(Collectors.toList());

        // 2. 점수 기반 정렬
        intersected.sort((r1, r2) -> {
            double diff1 = Math.abs(r1.getAvgTime1() - r1.getAvgTime2());
            double diff2 = Math.abs(r2.getAvgTime1() - r2.getAvgTime2());

            double score1 = (diff1 > 30) ? 0 : (
                    0.25 * (1 - (diff1 / 30.0)) +
                            0.35 * Math.max(0, (1 - ((r1.getAvgTime1() + r1.getAvgTime2()) / 120.0))) +
                            0.40 * Math.log10(r1.getTotalMobility1() + r1.getTotalMobility2())
            );

            double score2 = (diff2 > 30) ? 0 : (
                    0.25 * (1 - (diff2 / 30.0)) +
                            0.35 * Math.max(0, (1 - ((r2.getAvgTime1() + r2.getAvgTime2()) / 120.0))) +
                            0.40 * Math.log10(r2.getTotalMobility1() + r2.getTotalMobility2())
            );

            return Double.compare(score2, score1);
        });

        // 2. 나머지: totalMobility 기준 내림차순
        Comparator<MobilityResponse> byMobilityDesc = Comparator.comparingDouble(MobilityResponse::getTotalMobility).reversed();
        onlyFromFirst.sort(byMobilityDesc);
        onlyFromSecond.sort(byMobilityDesc);

        MultiMobilityResponse result = MultiMobilityResponse.builder()
                .firstMobility(onlyFromFirst)
                .secondMobility(onlyFromSecond)
                .intersectedMobility(intersected)
                .build();

        return ResponseDTO.res(HttpStatus.OK, "출발지 데이터 병합 완료", result);
    }

}
