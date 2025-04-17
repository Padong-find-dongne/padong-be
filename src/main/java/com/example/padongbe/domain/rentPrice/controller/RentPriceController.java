package com.example.padongbe.domain.rentPrice.controller;

import com.example.padongbe.domain.rentPrice.dto.response.RentPriceDto;
import com.example.padongbe.domain.rentPrice.service.RentPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentPrice")
@Tag(name = "RentPrice", description = "전월세가 관련 API")
public class RentPriceController {
    private final RentPriceService rentPriceService;

    @GetMapping("/data")
    @Operation(summary = "전월세가 데이터 저장")
    public ResponseEntity<String> getRentPriceData() {
        rentPriceService.getRentPriceData();
        return ResponseEntity.ok("Success to save rent price data");
    }

    @GetMapping("/{buildingType}/{dongneId}")
    @Operation(summary = "건물유형별 전월세가 조회")
    @Parameter(name = "buildingType", description = "건물 유형 (예: apartment, officetel, villa)")
    public ResponseEntity<RentPriceDto> getRentPriceByDongneId(@PathVariable String buildingType, @PathVariable Long dongneId) {
        return ResponseEntity.ok(rentPriceService.getRentPriceByDongneId(dongneId, buildingType));
    }

}
