package com.BCNC.Test.integration;

import com.BCNC.Test.entity.Album;
import com.BCNC.Test.service.strategy.EnrichingStrategy;

import java.util.List;

public class TestEnrichingStrategy implements EnrichingStrategy {
    private final List<Album> albumsEnriched;

    public TestEnrichingStrategy(List<Album> albumsEnriched) {
        this.albumsEnriched = albumsEnriched;
    }

    @Override
    public List<Album> enrich() {
        return albumsEnriched;
    }
}