package com.example.padongbe.domain.rentPrice.controller;

import com.example.padongbe.domain.rentPrice.service.RentPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentPrice")
public class RentPriceController {
    private final RentPriceService rentPriceService;

    @GetMapping("/data")
    public String getRentPriceData() {
        return rentPriceService.getRentPriceData();
    }

}
