package com.example.padongbe.domain.boundary.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BoundaryResponse {
    List<List<Double>> points;
}
