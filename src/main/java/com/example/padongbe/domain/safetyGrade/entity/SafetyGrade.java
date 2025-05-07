package com.example.padongbe.domain.safetyGrade.entity;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafetyGrade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String districtName;
    private double avgGrade;


    @OneToMany(mappedBy = "safetyGrade")
    private List<AdminDong> adminDongs = new ArrayList<>();
}
