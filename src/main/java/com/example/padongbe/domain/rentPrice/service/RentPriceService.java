package com.example.padongbe.domain.rentPrice.service;

import com.example.padongbe.domain.rentPrice.entity.RentPrice;
import com.example.padongbe.domain.rentPrice.repository.RentPriceRepository;
import com.example.padongbe.domain.rentPrice.util.RentPriceDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentPriceService {

    private final RentPriceRepository rentPriceRepository;

    public String getRentPriceData() {
        List<RentPrice> rentPriceList = RentPriceDataUtil.readRentPricesFromExcel();
        rentPriceRepository.saveAll(rentPriceList);
        return "Success to save rent price data";
    }
}
