package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.asset.Asset;

import java.util.List;
import java.util.UUID;

public interface AssetService {
    Asset create(Asset asset);

    List<Asset> findAll();

    Asset update(UUID id, Asset asset);

    Asset findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
