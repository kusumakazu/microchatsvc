package com.id.kusumakazu.repository;

import com.id.kusumakazu.domain.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Asset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {}
