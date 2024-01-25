package com.id.kusumakazu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.id.kusumakazu.IntegrationTest;
import com.id.kusumakazu.domain.Asset;
import com.id.kusumakazu.repository.AssetRepository;
import com.id.kusumakazu.service.dto.AssetDTO;
import com.id.kusumakazu.service.mapper.AssetMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ASSET = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ASSET = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ASSET_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ASSET_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private MockMvc restAssetMockMvc;

    private Asset asset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createEntity() {
        Asset asset = new Asset()
            .uuid(DEFAULT_UUID)
            .assetName(DEFAULT_ASSET_NAME)
            .assetType(DEFAULT_ASSET_TYPE)
            .asset(DEFAULT_ASSET)
            .assetContentType(DEFAULT_ASSET_CONTENT_TYPE);
        return asset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createUpdatedEntity() {
        Asset asset = new Asset()
            .uuid(UPDATED_UUID)
            .assetName(UPDATED_ASSET_NAME)
            .assetType(UPDATED_ASSET_TYPE)
            .asset(UPDATED_ASSET)
            .assetContentType(UPDATED_ASSET_CONTENT_TYPE);
        return asset;
    }

    @BeforeEach
    public void initTest() {
        assetRepository.deleteAll();
        asset = createEntity();
    }

    @Test
    void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();
        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);
        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isCreated());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate + 1);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testAsset.getAssetName()).isEqualTo(DEFAULT_ASSET_NAME);
        assertThat(testAsset.getAssetType()).isEqualTo(DEFAULT_ASSET_TYPE);
        assertThat(testAsset.getAsset()).isEqualTo(DEFAULT_ASSET);
        assertThat(testAsset.getAssetContentType()).isEqualTo(DEFAULT_ASSET_CONTENT_TYPE);
    }

    @Test
    void createAssetWithExistingId() throws Exception {
        // Create the Asset with an existing ID
        asset.setId("existing_id");
        AssetDTO assetDTO = assetMapper.toDto(asset);

        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAssets() throws Exception {
        // Initialize the database
        assetRepository.save(asset);

        // Get all the assetList
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].assetName").value(hasItem(DEFAULT_ASSET_NAME.toString())))
            .andExpect(jsonPath("$.[*].assetType").value(hasItem(DEFAULT_ASSET_TYPE)))
            .andExpect(jsonPath("$.[*].assetContentType").value(hasItem(DEFAULT_ASSET_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].asset").value(hasItem(Base64Utils.encodeToString(DEFAULT_ASSET))));
    }

    @Test
    void getAsset() throws Exception {
        // Initialize the database
        assetRepository.save(asset);

        // Get the asset
        restAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asset.getId()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.assetName").value(DEFAULT_ASSET_NAME.toString()))
            .andExpect(jsonPath("$.assetType").value(DEFAULT_ASSET_TYPE))
            .andExpect(jsonPath("$.assetContentType").value(DEFAULT_ASSET_CONTENT_TYPE))
            .andExpect(jsonPath("$.asset").value(Base64Utils.encodeToString(DEFAULT_ASSET)));
    }

    @Test
    void getNonExistingAsset() throws Exception {
        // Get the asset
        restAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingAsset() throws Exception {
        // Initialize the database
        assetRepository.save(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset
        Asset updatedAsset = assetRepository.findById(asset.getId()).get();
        updatedAsset
            .uuid(UPDATED_UUID)
            .assetName(UPDATED_ASSET_NAME)
            .assetType(UPDATED_ASSET_TYPE)
            .asset(UPDATED_ASSET)
            .assetContentType(UPDATED_ASSET_CONTENT_TYPE);
        AssetDTO assetDTO = assetMapper.toDto(updatedAsset);

        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testAsset.getAssetName()).isEqualTo(UPDATED_ASSET_NAME);
        assertThat(testAsset.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
        assertThat(testAsset.getAsset()).isEqualTo(UPDATED_ASSET);
        assertThat(testAsset.getAssetContentType()).isEqualTo(UPDATED_ASSET_CONTENT_TYPE);
    }

    @Test
    void putNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(UUID.randomUUID().toString());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(UUID.randomUUID().toString());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(UUID.randomUUID().toString());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.save(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset.uuid(UPDATED_UUID).assetType(UPDATED_ASSET_TYPE);

        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testAsset.getAssetName()).isEqualTo(DEFAULT_ASSET_NAME);
        assertThat(testAsset.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
        assertThat(testAsset.getAsset()).isEqualTo(DEFAULT_ASSET);
        assertThat(testAsset.getAssetContentType()).isEqualTo(DEFAULT_ASSET_CONTENT_TYPE);
    }

    @Test
    void fullUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.save(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset
            .uuid(UPDATED_UUID)
            .assetName(UPDATED_ASSET_NAME)
            .assetType(UPDATED_ASSET_TYPE)
            .asset(UPDATED_ASSET)
            .assetContentType(UPDATED_ASSET_CONTENT_TYPE);

        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testAsset.getAssetName()).isEqualTo(UPDATED_ASSET_NAME);
        assertThat(testAsset.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
        assertThat(testAsset.getAsset()).isEqualTo(UPDATED_ASSET);
        assertThat(testAsset.getAssetContentType()).isEqualTo(UPDATED_ASSET_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(UUID.randomUUID().toString());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(UUID.randomUUID().toString());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(UUID.randomUUID().toString());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAsset() throws Exception {
        // Initialize the database
        assetRepository.save(asset);

        int databaseSizeBeforeDelete = assetRepository.findAll().size();

        // Delete the asset
        restAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, asset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
