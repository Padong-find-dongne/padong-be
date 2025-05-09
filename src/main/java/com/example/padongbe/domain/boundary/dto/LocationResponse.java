package com.example.padongbe.domain.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationResponse {
  private String address;
  private double lat;
  private double lng;
}
