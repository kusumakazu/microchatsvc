package com.id.kusumakazu.service;

import com.id.kusumakazu.service.dto.AssetDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.id.kusumakazu.domain.Asset}.
 */
public interface AssetService {
    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save.
     * @return the persisted entity.
     */
    AssetDTO save(AssetDTO assetDTO);

    /**
     * Updates a asset.
     *
     * @param assetDTO the entity to update.
     * @return the persisted entity.
     */
    AssetDTO update(AssetDTO assetDTO);

    /**
     * Partially updates a asset.
     *
     * @param assetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetDTO> partialUpdate(AssetDTO assetDTO);

    /**
     * Get all the assets.
     *
     * @return the list of entities.
     */
    List<AssetDTO> findAll();

    /**
     * Get the "id" asset.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetDTO> findOne(String id);

    /**
     * Delete the "id" asset.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
