package com.example.padongbe.boundary.controller;

import com.example.padongbe.boundary.dto.BoundaryResponse;
import com.example.padongbe.boundary.service.BoundaryService;
import com.example.padongbe.global.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/boundary")
public class BoundaryController {
  private final BoundaryService boundaryService;
  @GetMapping("/{geocode}")
  public ResponseEntity<ResponseDTO<BoundaryResponse>> getBoundary(@PathVariable String geocode){
    return ResponseEntity.ok(boundaryService.getBoundary(geocode));
  }
}
