package com.BCNC.Test.service.strategy;
import com.BCNC.Test.entity.Album;

import java.util.List;

public interface EnrichingStrategy {
    List<Album> enrich();
}
