package com.example.padongbe.domain.rentPrice.repository;

import com.example.padongbe.domain.rentPrice.entity.RentPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentPriceRepository extends JpaRepository<RentPrice, Long> {
    Optional<RentPrice> findByDistrictCodeAndDongCodeAndBuildingType(String districtCode, String dongCode, String buildingType);
}
