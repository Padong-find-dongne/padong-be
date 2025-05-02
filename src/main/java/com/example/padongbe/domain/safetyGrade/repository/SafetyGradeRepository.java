package com.example.padongbe.domain.safetyGrade.repository;


import com.example.padongbe.domain.safetyGrade.entity.SafetyGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SafetyGradeRepository extends JpaRepository<SafetyGrade, Long> {
    Optional<SafetyGrade> findByDistrictName(String districtName);
}
