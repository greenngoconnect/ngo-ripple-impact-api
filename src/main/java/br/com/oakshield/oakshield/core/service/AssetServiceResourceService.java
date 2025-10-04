package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.asset.AssetServiceResource;

import java.util.List;
import java.util.UUID;

public interface AssetServiceResourceService {
    AssetServiceResource create(AssetServiceResource assetServiceResource);

    List<AssetServiceResource> findAll();

    AssetServiceResource findById(UUID id);

    void deleteById(UUID id);

}
