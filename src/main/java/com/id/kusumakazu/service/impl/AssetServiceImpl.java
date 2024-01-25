package com.id.kusumakazu.service.impl;

import com.id.kusumakazu.domain.Asset;
import com.id.kusumakazu.repository.AssetRepository;
import com.id.kusumakazu.service.AssetService;
import com.id.kusumakazu.service.dto.AssetDTO;
import com.id.kusumakazu.service.mapper.AssetMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Asset}.
 */
@Service
public class AssetServiceImpl implements AssetService {

    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);

    private final AssetRepository assetRepository;

    private final AssetMapper assetMapper;

    public AssetServiceImpl(AssetRepository assetRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
    }

    @Override
    public AssetDTO save(AssetDTO assetDTO) {
        log.debug("Request to save Asset : {}", assetDTO);
        Asset asset = assetMapper.toEntity(assetDTO);
        asset = assetRepository.save(asset);
        return assetMapper.toDto(asset);
    }

    @Override
    public AssetDTO update(AssetDTO assetDTO) {
        log.debug("Request to update Asset : {}", assetDTO);
        Asset asset = assetMapper.toEntity(assetDTO);
        asset = assetRepository.save(asset);
        return assetMapper.toDto(asset);
    }

    @Override
    public Optional<AssetDTO> partialUpdate(AssetDTO assetDTO) {
        log.debug("Request to partially update Asset : {}", assetDTO);

        return assetRepository
            .findById(assetDTO.getId())
            .map(existingAsset -> {
                assetMapper.partialUpdate(existingAsset, assetDTO);

                return existingAsset;
            })
            .map(assetRepository::save)
            .map(assetMapper::toDto);
    }

    @Override
    public List<AssetDTO> findAll() {
        log.debug("Request to get all Assets");
        return assetRepository.findAll().stream().map(assetMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<AssetDTO> findOne(String id) {
        log.debug("Request to get Asset : {}", id);
        return assetRepository.findById(id).map(assetMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Asset : {}", id);
        assetRepository.deleteById(id);
    }
}
