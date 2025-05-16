package com.example.padongbe.domain.safetyGrade.service;

import com.example.padongbe.domain.safetyGrade.entity.SafetyGrade;
import com.example.padongbe.domain.safetyGrade.repository.SafetyGradeRepository;
import com.example.padongbe.domain.safetyGrade.util.SafetyGradeDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SafetyGradeService {

    private final SafetyGradeRepository safetyGradeRepository;
    private final SafetyGradeDataUtil safetyGradeDataUtil;

    public void uploadSafetyGrade() {
        List<SafetyGrade> safetyGradeList = safetyGradeDataUtil.readSafetyGradeFromExcel();
        safetyGradeRepository.saveAll(safetyGradeList);
    }

    public SafetyGrade getSafetyGradeByDistrict(String districtCode) {
        return safetyGradeRepository.findById(Long.valueOf(districtCode))
                .orElseThrow(() -> new IllegalArgumentException("Not Found Safety Grade"));
    }

    public Map<String, SafetyGrade> getSafetyGradeMap() {
        return safetyGradeRepository.findAll().stream()
                .collect(Collectors.toMap(SafetyGrade::getDistrictName, safetyGrade -> safetyGrade));
    }
}
