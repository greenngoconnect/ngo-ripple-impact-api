package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.asset.AssetTag;

import java.util.List;
import java.util.UUID;

public interface AssetTagService {
    AssetTag create(AssetTag assetTag);

    List<AssetTag> findAll();

    AssetTag update(UUID id, AssetTag assetTag);

    AssetTag findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
