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

        monthlyRentMin = rentPriceRepository.findMinAvgMonthlyRent();
        monthlyRentMax = rentPriceRepository.findMaxAvgMonthlyRent();

        monthlyDepositMin = rentPriceRepository.findMinAvgMonthlyDeposit();
        monthlyDepositMax = rentPriceRepository.findMaxAvgMonthlyDeposit();

        jeonseMin = rentPriceRepository.findMinAvgJeonseDeposit();
        jeonseMax = rentPriceRepository.findMaxAvgJeonseDeposit();
    }

    public double calculateScore(
            double totalMobility,
            double avgTime,
            double density,
            double safety, // 평균등급 (1~5)
            RentPriceDto rentPriceDto
    ) {
        // 1. 정규화
        double mobilityNorm = boost(normalize(totalMobility, mobilityMin, mobilityMax));
        double timeNorm = boost(1 - normalize(avgTime, timeMin, timeMax));
        double densityNorm = boost(1 - normalize(density, densityMin, densityMax));

        double monthlyNorm = boost(1 - normalize(rentPriceDto.getAvgMonthlyRent(), monthlyRentMin, monthlyRentMax));
        double depositNorm = boost(1 - normalize(rentPriceDto.getAvgMonthlyDeposit(), monthlyDepositMin, monthlyDepositMax));
        double jeonseNorm = boost(1 - normalize(rentPriceDto.getAvgJeonseDeposit(), jeonseMin, jeonseMax));
        double rentNorm = (monthlyNorm + depositNorm + jeonseNorm) / 3.0;


        // 2. 안전 등급 점수 (1등급 = 2점, 5등급 = 0점)
        double safetyScore = Math.max(0, (5.0 - safety) * 0.75); // 1등급 = 3.0

        // 3. 총합 점수 (10점 만점)
        double rawScore =
                mobilityNorm * 5.0 +
                        timeNorm * 2.0 +
                        densityNorm +
                        rentNorm * 1.5 +
                        safetyScore;  // safetyScore는 0 ~ 3.0 (1등급 기준)

        return Math.round(adjustForDisplay(rawScore) * 100) / 100.0;
    }

    private double normalize(double value, double min, double max) {
        if (max == min) return 0.5;
        return (value - min) / (max - min);
    }

    private double boost(double value) {
        return Math.pow(value, 0.2); // 예: 0.6 → 0.68, 0.8 → 0.86
    }

    private double adjustForDisplay(double rawScore) {
        if (rawScore <= 6.0) {
            return 6.0 + (rawScore / 6.0) * 2.0; // 0~6 → 6~8
        } else if (rawScore <= 9.0) {
            return 8.0 + ((rawScore - 6.0) / 3.0); // 6~9 → 8~9
        } else {
            return 9.0 + ((rawScore - 9.0) / 3.5); // 9~12.5 → 9~10
        }
    }

}
