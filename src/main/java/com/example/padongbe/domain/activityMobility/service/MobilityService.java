package com.example.padongbe.domain.activityMobility.service;

import com.example.padongbe.domain.activityMobility.dto.IntersectedMobilityResponse;
import com.example.padongbe.domain.activityMobility.dto.MobilityResponse;
import com.example.padongbe.domain.activityMobility.dto.MultiMobilityResponse;
import com.example.padongbe.domain.activityMobility.entity.Mobility;
import com.example.padongbe.domain.activityMobility.repository.MobilityRepository;
import com.example.padongbe.domain.dongne.dto.AdminDongDto;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.service.DongneService;
import com.example.padongbe.domain.population.service.PopulationService;
import com.example.padongbe.domain.rentPrice.dto.response.RentPriceDto;
import com.example.padongbe.domain.rentPrice.service.RentPriceService;
import com.example.padongbe.domain.score.service.ScoreCalculator;
import com.example.padongbe.global.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.padongbe.domain.activityMobility.util.ActivityMobilityDataUtil.readActivityMobilityFromExcel;

@Service
@RequiredArgsConstructor
public class MobilityService {

    private final MobilityRepository mobilityRepository;
    private final DongneService dongneService;
    private final PopulationService populationService;
    private final RentPriceService rentPriceService;
    private final ScoreCalculator scoreCalculator;

    public List<MobilityResponse> mapFromEntities(Collection<Mobility> mobilities) {
        List<MobilityResponse> responses = new ArrayList<>();
        for (Mobility mobility : mobilities) {
            AdminDong departureDong = mobility.getDepartureDong();
            double safety = departureDong.getSafetyGrade().getAvgGrade();
            double density = populationService.getPopulationByAdmin(departureDong).getDensity();
            double avgTime = mobility.getAvgTime();
            double totalMobility = mobility.getTotalMobility();
            RentPriceDto rentPriceDto = rentPriceService.getRentPriceByAdminDongCode(departureDong.getAdminDongCode());
            double score = scoreCalculator.calculateScore(totalMobility, avgTime, density, safety, rentPriceDto);

            responses.add(MobilityResponse.builder()
                    .departureDong(AdminDongDto.builder()
                            .adminDongCode(departureDong.getAdminDongCode())
                            .address(departureDong.getCity() + " " + departureDong.getDistrict() + " " + departureDong.getAdminAreaName())
                            .build())
                    .totalMobility(Math.round(totalMobility * 100) / 100.0)
                    .avgTime(Math.round(avgTime * 100) / 100.0)
                    .density(Math.round(density * 100) / 100.0)
                    .safety(Math.round(safety * 100) / 100.0)
                    .avgJeonseDeposit(Math.round(rentPriceDto.getAvgJeonseDeposit() * 100) / 100.0)
                    .avgMonthlyDeposit(Math.round(rentPriceDto.getAvgMonthlyDeposit() * 100) / 100.0)
                    .avgMonthlyRent(Math.round(rentPriceDto.getAvgMonthlyRent() * 100) / 100.0)
                    .score(score)
                    .build());
        }
        return responses;
    }

    public void fetchData() {
        mobilityRepository.saveAll(readActivityMobilityFromExcel());
    }

    public ResponseDTO<List<MobilityResponse>> searchByAddress(String address, Pageable pageable) {
        AdminDong adminDong = dongneService.findAdminDongByAddress(address);
        Page<Mobility> mobilities = mobilityRepository.findByArrivalDong(adminDong, pageable);
        if (mobilities.isEmpty())
            return ResponseDTO.res(HttpStatus.NOT_FOUND, "해당 행정동이 존재하지 않습니다.");

        return ResponseDTO.res(HttpStatus.OK, "출발 행정동 조회 성공", mapFromEntities(mobilities.getContent()));
    }

    public Mobility findByGeoCodes(String arrivalCode, String departureCode){
        AdminDong arrivalDong= dongneService.findAdminDongByCode(arrivalCode);
        AdminDong departureDong= dongneService.findAdminDongByCode(departureCode);
        Optional<Mobility>optional= mobilityRepository.findByArrivalDongAndDepartureDong(arrivalDong,departureDong);
      return optional.orElse(null);
    }
    public ResponseDTO<MultiMobilityResponse> searchByTwoAddresses(String address1, String address2, int page) {
        AdminDong adminDong1 = dongneService.findAdminDongByAddress(address1);
        AdminDong adminDong2 = dongneService.findAdminDongByAddress(address2);

        List<Mobility> entities1 = mobilityRepository.findByArrivalDong(adminDong1);
        List<Mobility> entities2 = mobilityRepository.findByArrivalDong(adminDong2);


        Map<String, MobilityResponse> map1 = mapFromEntities(entities1).stream()
                .collect(Collectors.toMap(
                        r -> r.getDepartureDong().getAdminDongCode(),
                        Function.identity(),
                        (a, b) -> a
                ));

        Map<String, MobilityResponse> map2 = mapFromEntities(entities2).stream()
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
                .firstMobility(listToPage(onlyFromFirst, page))
                .secondMobility(listToPage(onlyFromSecond, page))
                .intersectedMobility(listToPage(intersected, page))
                .build();

        return ResponseDTO.res(HttpStatus.OK, "출발지 데이터 병합 완료", result);
    }

    public <T> List<T> listToPage(List<T> list, int page) {
        int totalElements = list.size();
        int fromIndex = (page + 1) * 10;
        int toIndex = Math.min(fromIndex + 10, totalElements);

        if (fromIndex <= totalElements) return list.subList(fromIndex, toIndex);
        return null;
    }

    private double normalize(double value, double min, double max) {
        if (max == min) return 0.5;
        return (value - min) / (max - min);
    }

}
