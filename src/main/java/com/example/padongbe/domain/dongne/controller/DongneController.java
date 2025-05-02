package com.example.padongbe.domain.dongne.controller;

import com.example.padongbe.domain.dongne.service.DongneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dongne")
@Tag(name = "Dongne", description = "동네 관련 API")
public class DongneController {

    private final DongneService dongneService;

    @PostMapping("/data")
    @Operation(summary = "동네 데이터 추가")
    public ResponseEntity<String> addDongneData(){
        dongneService.addDongneDate();
        return ResponseEntity.ok("동네 데이터 추가 완료");
    }

    @PostMapping("/data/safetyGrade")
    @Operation(summary = "동네 안전등급 데이터 추가")
    public ResponseEntity<String> addDongneSafetyGradeData(){
        dongneService.addDongneSafetyGradeData();
        return ResponseEntity.ok("동네 안전등급 데이터 추가 완료");
    }
}
