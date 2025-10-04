package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.asset.AssetType;

import java.util.List;
import java.util.UUID;

public interface AssetTypeService {
    AssetType create(AssetType assetType);

    AssetType update(UUID id, AssetType assetType);

    List<AssetType> findAll();

    AssetType findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
