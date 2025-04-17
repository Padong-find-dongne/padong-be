package com.example.padongbe.domain.rentPrice.service;

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

    public void getRentPriceData() {
        List<RentPrice> rentPriceList = RentPriceDataUtil.readRentPricesFromExcel();
        rentPriceRepository.saveAll(rentPriceList);
    }

    public RentPriceDto getRentPriceByDongneId(Long dongneId, String buildingType) {
        String dongne = dongneId.toString();
        String districtCode = dongne.substring(0, 5);
        String dongCode = dongne.substring(5, 10);

        Optional<RentPrice> rentPriceOpt = rentPriceRepository.
                findByDistrictCodeAndDongCodeAndBuildingType(districtCode, dongCode, convertBuildingType(buildingType));
        if (rentPriceOpt.isEmpty()) {
            log.error("No data found for dongneId: {}", dongneId);
            throw new IllegalArgumentException("No data found for the given dongneId");
        }
        return new RentPriceDto(rentPriceOpt.get());
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
