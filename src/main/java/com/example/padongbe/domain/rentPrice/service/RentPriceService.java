package com.example.padongbe.domain.rentPrice.service;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.entity.DongMapping;
import com.example.padongbe.domain.dongne.entity.LegalDong;
import com.example.padongbe.domain.dongne.service.DongneService;
import com.example.padongbe.domain.rentPrice.dto.response.RentPriceDto;
import com.example.padongbe.domain.rentPrice.entity.RentPrice;
import com.example.padongbe.domain.rentPrice.repository.RentPriceRepository;
import com.example.padongbe.domain.rentPrice.util.RentPriceDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentPriceService {

    private final RentPriceRepository rentPriceRepository;
    private final DongneService dongneService;

    public void uploadRentPriceData() {
        List<RentPrice> rentPriceList = RentPriceDataUtil.readRentPricesFromExcel();
        rentPriceRepository.saveAll(rentPriceList);
    }

    public RentPriceDto getRentPriceByAdminDongCode(String adminDongCode, String buildingType) {
        List<DongMapping> dongMappingList = dongneService.findAdminDongByCode(adminDongCode).getDongMappingList();
        String convertedBuildingType = convertBuildingType(buildingType);
        Long sumMonthlyRent = 0L;
        Long sumJeonseDeposit = 0L;
        Long sumMonthlyDeposit = 0L;
        int count = 0;

        for (DongMapping dongMapping : dongMappingList) {
            Optional<RentPrice> rentPriceOpt = rentPriceRepository.findByLegalDongAndBuildingType(dongMapping.getLegalDong(), convertedBuildingType);
            if (rentPriceOpt.isPresent()) {
                RentPrice rentPrice = rentPriceOpt.get();
                sumMonthlyRent += rentPrice.getAvgMonthlyRent();
                sumJeonseDeposit += rentPrice.getAvgJeonseDeposit();
                sumMonthlyDeposit += rentPrice.getAvgMonthlyDeposit();
                count++;
            }
        }

        Long avgMonthlyRent = count > 0 ? sumMonthlyRent / count : 0;
        Long avgJeonseDeposit = count > 0 ? sumJeonseDeposit / count : 0;
        Long avgMonthlyDeposit = count > 0 ? sumMonthlyDeposit / count : 0;

        return RentPriceDto.builder()
                .adminDongCode(adminDongCode)
                .buildingType(buildingType)
                .avgMonthlyRent(avgMonthlyRent)
                .avgJeonseDeposit(avgJeonseDeposit)
                .avgMonthlyDeposit(avgMonthlyDeposit)
                .build();
    }

    private String convertBuildingType(String buildingType) {
        return switch (buildingType) {
            case "apartment" -> "아파트";
            case "officetel" -> "오피스텔";
            case "villa" -> "주택";
            default -> throw new IllegalStateException("Unexpected value: " + buildingType);
        };
    }
}
