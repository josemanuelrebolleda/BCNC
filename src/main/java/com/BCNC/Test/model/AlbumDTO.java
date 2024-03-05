package com.BCNC.Test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDTO {
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    }