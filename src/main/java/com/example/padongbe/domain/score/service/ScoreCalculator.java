package com.example.padongbe.domain.score.service;

import com.example.padongbe.domain.activityMobility.repository.MobilityRepository;
import com.example.padongbe.domain.population.repository.PopulationRepository;
import com.example.padongbe.domain.rentPrice.dto.response.RentPriceDto;
import com.example.padongbe.domain.rentPrice.repository.RentPriceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoreCalculator {

    private final MobilityRepository mobilityRepository;
    private final RentPriceRepository rentPriceRepository;
    private final PopulationRepository populationRepository;

    // 정규화 기준 값
    private double mobilityMin, mobilityMax;
    private double timeMin, timeMax;
    private double densityMin, densityMax;
    private double monthlyRentMin, monthlyRentMax;
    private double monthlyDepositMin, monthlyDepositMax;
    private double jeonseMin, jeonseMax;


    @PostConstruct
    public void init() {
        mobilityMin = mobilityRepository.findMinMobility();
        mobilityMax = mobilityRepository.findMaxMobility();

        timeMin = mobilityRepository.findMinAvgTime();
        timeMax = mobilityRepository.findMaxAvgTime();

        densityMin = populationRepository.findMinDensity();
        densityMax = populationRepository.findMaxDensity();

        // 점수에 가격 제외
//        monthlyRentMin = rentPriceRepository.findMinAvgMonthlyRent();
//        monthlyRentMax = rentPriceRepository.findMaxAvgMonthlyRent();
//
//        monthlyDepositMin = rentPriceRepository.findMinAvgMonthlyDeposit();
//        monthlyDepositMax = rentPriceRepository.findMaxAvgMonthlyDeposit();
//
//        jeonseMin = rentPriceRepository.findMinAvgJeonseDeposit();
//        jeonseMax = rentPriceRepository.findMaxAvgJeonseDeposit();
    }

    public double calculateScore(
            double totalMobility,
            double avgTime,
            double density,
            double safety,
            RentPriceDto rentPriceDto
    ) {
        // 1. 정규화
        double mobilityNorm = boost(normalizeAroundMean(totalMobility, mobilityMin, mobilityMax));
        double timeNorm = boost(1 - normalizeAroundMean(avgTime, timeMin, timeMax));
        double densityNorm = boost(1 - normalizeAroundMean(density, densityMin, densityMax));

//        double monthlyNorm = boost(1 - normalize(rentPriceDto.getAvgMonthlyRent(), monthlyRentMin, monthlyRentMax));
//        double depositNorm = boost(1 - normalize(rentPriceDto.getAvgMonthlyDeposit(), monthlyDepositMin, monthlyDepositMax));
//        double jeonseNorm = boost(1 - normalize(rentPriceDto.getAvgJeonseDeposit(), jeonseMin, jeonseMax));
//        double rentNorm = (monthlyNorm + depositNorm + jeonseNorm) / 3.0;

        // 만점: 1.5점 (1등급), 최하: 0점 (5등급)
        double safetyScore = Math.max(0, (5.0 - safety) * 0.375);  // (1.5 / 4)

        double rawScore =
                mobilityNorm * 5.0 +
                        timeNorm * 2.0 +
                        densityNorm * 1.5 +
                        safetyScore;

        double finalScore = 6.0 + (rawScore / 9.5) * 3.8;
        finalScore = Math.min(finalScore, 9.5);
        return Math.round(finalScore * 100) / 100.0;
    }

    // 예: 평균을 기준으로 한 양방향 정규화
    private double normalizeAroundMean(double value, double mean, double stddev) {
        return 0.5 + (value - mean) / (2 * stddev); // 대략 0~1 분포 유지
    }


    // boost 함수 완화 또는 제거
    private double boost(double value) {
        return Math.pow(value, 0.7); // 또는 그냥 return value;
    }

}
