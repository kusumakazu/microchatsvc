package com.id.kusumakazu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetMapperTest {

    private AssetMapper assetMapper;

    @BeforeEach
    public void setUp() {
        assetMapper = new AssetMapperImpl();
    }
}
