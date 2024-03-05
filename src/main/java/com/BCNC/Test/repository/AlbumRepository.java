package com.BCNC.Test.repository;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.model.AlbumDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
