package com.example.padongbe.domain.rentPrice.repository;

import com.example.padongbe.domain.rentPrice.entity.RentPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentPriceRepository extends JpaRepository<RentPrice, Long> {
}
