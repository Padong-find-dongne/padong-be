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

    public RentPriceDto getRentPriceByDongneId(Long dongneId) {
        String dongne = dongneId.toString();
        Long districtCode = Long.parseLong(dongne.substring(0, 5));
        Long dongCode = Long.parseLong(dongne.substring(5, 10));

        Optional<RentPrice> rentPriceOpt = rentPriceRepository.findByDistrictCodeAndDongCode(districtCode, dongCode);
        if (rentPriceOpt.isEmpty()) {
            log.error("No data found for dongneId: {}", dongneId);
            throw new IllegalArgumentException("No data found for the given dongneId");
        }
        return new RentPriceDto(rentPriceOpt.get());
    }
}
