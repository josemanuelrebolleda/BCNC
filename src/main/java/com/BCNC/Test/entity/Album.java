package com.BCNC.Test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Album {
    @Id
    private Long id;
    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private List<Photo> photos;

    public Album(Album album) {
        this.id = album.id;
        this.title = album.title;
        this.photos = album.photos;
    }
}
