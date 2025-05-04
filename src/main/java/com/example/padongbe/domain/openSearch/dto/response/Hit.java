package com.example.padongbe.domain.openSearch.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Hit {
    @JsonProperty("_source")
    private AdminDongSearch source;
}