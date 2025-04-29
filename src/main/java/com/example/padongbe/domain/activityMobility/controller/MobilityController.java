package com.example.padongbe.domain.activityMobility.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobility")
@RequiredArgsConstructor
@Tag(name = "Mobility", description = "서울시 생활이동 데이터 관리 API")
public class MobilityController {

}
