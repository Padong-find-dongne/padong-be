package com.example.padongbe.domain.rentPrice.controller;

import com.example.padongbe.domain.rentPrice.dto.response.RentPriceDto;
import com.example.padongbe.domain.rentPrice.service.RentPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentPrice")
public class RentPriceController {
    private final RentPriceService rentPriceService;

    @GetMapping("/data")
    public ResponseEntity<String> getRentPriceData() {
        rentPriceService.getRentPriceData();
        return ResponseEntity.ok("Success to save rent price data");
    }

    @GetMapping("/{buildingType}/{dongneId}")
    public ResponseEntity<RentPriceDto> getRentPriceByDongneId(@PathVariable String buildingType, @PathVariable Long dongneId) {
        return ResponseEntity.ok(rentPriceService.getRentPriceByDongneId(dongneId, buildingType));
    }

}
