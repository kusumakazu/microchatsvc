package com.id.kusumakazu.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.id.kusumakazu.domain.Asset} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetDTO implements Serializable {

    private String id;

    private String uuid;

    private String assetName;

    private String assetType;

    private byte[] asset;

    private String assetContentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public byte[] getAsset() {
        return asset;
    }

    public void setAsset(byte[] asset) {
        this.asset = asset;
    }

    public String getAssetContentType() {
        return assetContentType;
    }

    public void setAssetContentType(String assetContentType) {
        this.assetContentType = assetContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDTO)) {
            return false;
        }

        AssetDTO assetDTO = (AssetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDTO{" +
            "id='" + getId() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", assetName='" + getAssetName() + "'" +
            ", assetType='" + getAssetType() + "'" +
            ", asset='" + getAsset() + "'" +
            "}";
    }
}
