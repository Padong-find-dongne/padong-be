package com.example.padongbe.domain.activityMobility.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MobilityResponse {
  private String departureCode;
  private double totalMobility;
  private double avgTime;
}
