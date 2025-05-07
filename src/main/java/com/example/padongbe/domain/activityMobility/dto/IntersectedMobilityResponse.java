package com.example.padongbe.domain.activityMobility.dto;

import com.example.padongbe.domain.dongne.dto.AdminDongDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IntersectedMobilityResponse {
    private AdminDongDto departureDong;
    private double totalMobility1;
    private double totalMobility2;
    private double avgTime1;
    private double avgTime2;
}
