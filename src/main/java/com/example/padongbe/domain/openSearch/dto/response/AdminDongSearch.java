package com.example.padongbe.domain.openSearch.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AdminDongSearch {
    private Long id;
    @JsonProperty("admin_area_name")
    private String adminAreaName;
    private String city;
    private String district;
}