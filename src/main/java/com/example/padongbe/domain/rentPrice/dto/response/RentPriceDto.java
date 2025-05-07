package com.example.padongbe.domain.rentPrice.dto.response;

import com.example.padongbe.domain.rentPrice.entity.RentPrice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class RentPriceDto {
    private String adminDongCode;
    private String buildingType;
    private Long avgJeonseDeposit;
    private Long avgMonthlyDeposit;
    private Long avgMonthlyRent;

}
