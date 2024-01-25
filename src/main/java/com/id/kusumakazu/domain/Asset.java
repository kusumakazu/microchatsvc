package com.id.kusumakazu.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Asset.
 */
@Document(collection = "asset")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("uuid")
    private String uuid;

    @Field("asset_name")
    private String assetName;

    @Field("asset_type")
    private String assetType;

    @Field("asset")
    private byte[] asset;

    @Field("asset_content_type")
    private String assetContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Asset id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Asset uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAssetName() {
        return this.assetName;
    }

    public Asset assetName(String assetName) {
        this.setAssetName(assetName);
        return this;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetType() {
        return this.assetType;
    }

    public Asset assetType(String assetType) {
        this.setAssetType(assetType);
        return this;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public byte[] getAsset() {
        return this.asset;
    }

    public Asset asset(byte[] asset) {
        this.setAsset(asset);
        return this;
    }

    public void setAsset(byte[] asset) {
        this.asset = asset;
    }

    public String getAssetContentType() {
        return this.assetContentType;
    }

    public Asset assetContentType(String assetContentType) {
        this.assetContentType = assetContentType;
        return this;
    }

    public void setAssetContentType(String assetContentType) {
        this.assetContentType = assetContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        return id != null && id.equals(((Asset) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asset{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", assetName='" + getAssetName() + "'" +
            ", assetType='" + getAssetType() + "'" +
            ", asset='" + getAsset() + "'" +
            ", assetContentType='" + getAssetContentType() + "'" +
            "}";
    }
}
