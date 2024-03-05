package com.BCNC.Test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Photo {
    @Id
    private Long id;
    private String title;
    private String url;
    private String thumbnailUrl;

    @Column(name = "album_id")
    private Long albumId;

}
