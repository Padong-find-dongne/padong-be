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
            double safety, // 평균등급 (1~5)
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

//        double safetyScore = Math.max(0, (5.0 - safety) * 0.75); // 1등급 = 3.0
        // 만점: 1.5점 (1등급), 최하: 0점 (5등급)
        double safetyScore = Math.max(0, (5.0 - safety) * 0.375);  // (1.5 / 4)


// 최종 점수 (10점 만점)

// 최종 점수 (10점 만점)
        double rawScore =
                mobilityNorm * 5.0 +
                        timeNorm * 2.0 +
                        densityNorm * 1.5+
                        safetyScore;

//        return Math.round(adjustForDisplay(finalScore) * 100) / 100.0;
//        return Math.round(finalScore * 100) / 100.0;
        double finalScore = Math.min(10.0, rawScore * 1.3 + 1.0);
        return Math.round(finalScore * 100) / 100.0;
    }

    private double normalize(double value, double min, double max) {
        if (max == min) return 0.5;
        return (value - min) / (max - min);
    }

    // 예: 평균을 기준으로 한 양방향 정규화
    private double normalizeAroundMean(double value, double mean, double stddev) {
        return 0.5 + (value - mean) / (2 * stddev); // 대략 0~1 분포 유지
    }


    // boost 함수 완화 또는 제거
    private double boost(double value) {
        return Math.pow(value, 0.7); // 또는 그냥 return value;
    }

    private double adjustForDisplay(double rawScore) {
        return Math.min(10.0, rawScore * 1.4 + 1.0);
    }


//    private double adjustForDisplay(double rawScore) {
//        if (rawScore <= 6.0) {
//            return 6.0 + (rawScore / 6.0) * 2.0; // 0~6 → 6~8
//        } else if (rawScore <= 9.0) {
//            return 8.0 + ((rawScore - 6.0) / 3.0); // 6~9 → 8~9
//        } else {
//            return 9.0 + ((rawScore - 9.0) / 3.5); // 9~12.5 → 9~10
//        }
//    }

}
