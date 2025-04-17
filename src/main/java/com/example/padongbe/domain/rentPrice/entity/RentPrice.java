package com.example.padongbe.domain.rentPrice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentPrice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dongName;
    private String districtName;
    private String districtCode;
    private String dongCode;
    private String buildingType;
    private Long avgJeonseDeposit;
    private Long avgMonthlyDeposit;
    private Long avgMonthlyRent;

}
