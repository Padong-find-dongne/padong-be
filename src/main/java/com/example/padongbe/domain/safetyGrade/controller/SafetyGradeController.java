package com.example.padongbe.domain.safetyGrade.controller;

import com.example.padongbe.domain.safetyGrade.service.SafetyGradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/safetyGrade")
@Tag(name = "SafetyGrade", description = "안전지수등급 관련 API")
public class SafetyGradeController {

    private final SafetyGradeService safetyGradeService;

    @PostMapping("/data")
    @Operation(summary = "안전지수등급 데이터 업로드")
    public ResponseEntity<String> uploadSafetyGrade() {
        safetyGradeService.uploadSafetyGrade();
        return ResponseEntity.ok("Success to upload safety grade data.");
    }
}
