package com.example.padongbe.domain.rentPrice.dto.response;

import com.example.padongbe.domain.rentPrice.entity.RentPrice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentPriceDto {
    private Long id;
    private String districtName;
    private String dongName;
    private String dongneId;
    private String buildingType;
    private Long avgJeonseDeposit;
    private Long avgMonthlyDeposit;
    private Long avgMonthlyRent;

    public RentPriceDto(RentPrice rentPrice) {
        this.id = rentPrice.getId();
        this.districtName = rentPrice.getDistrictName();
        this.dongName = rentPrice.getDongName();
        this.dongneId = rentPrice.getDistrictCode() + rentPrice.getDongCode();
        this.buildingType = rentPrice.getBuildingType();
        this.avgJeonseDeposit = rentPrice.getAvgJeonseDeposit();
        this.avgMonthlyDeposit = rentPrice.getAvgMonthlyDeposit();
        this.avgMonthlyRent = rentPrice.getAvgMonthlyRent();
    }
}
