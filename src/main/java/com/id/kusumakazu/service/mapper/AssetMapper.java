package com.id.kusumakazu.service.mapper;

import com.id.kusumakazu.domain.Asset;
import com.id.kusumakazu.service.dto.AssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asset} and its DTO {@link AssetDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {}
