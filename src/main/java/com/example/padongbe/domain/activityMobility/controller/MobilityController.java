package com.example.padongbe.domain.activityMobility.controller;

import com.example.padongbe.domain.activityMobility.dto.MobilityResponse;
import com.example.padongbe.domain.activityMobility.service.MobilityService;
import com.example.padongbe.global.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mobility")
@RequiredArgsConstructor
@Tag(name = "Mobility", description = "서울시 생활이동 데이터 관리 API")
public class MobilityController {
    private final MobilityService mobilityService;

    @GetMapping("/{arrivalCode}")
    public ResponseEntity<ResponseDTO<List<MobilityResponse>>> searchByDestination(@PathVariable String arrivalCode) {
        return ResponseEntity.ok(mobilityService.searchByDestination(arrivalCode));
    }

    @GetMapping("/address")
    @Operation(summary = "행정동 주소로 검색")
    @Parameter(name = "address", description = "행정동 주소", example = "서울특별시 서대문구 남가좌1동")
    public ResponseEntity<ResponseDTO<List<MobilityResponse>>> searchByAddress(@RequestBody String address) {
        return ResponseEntity.ok(mobilityService.searchByAddress(address));
    }

    @PostMapping("/data")
    @Operation(summary = "서울시 생활이동 데이터 저장")
    public void fetchData() {
        mobilityService.fetchData();
    }

}