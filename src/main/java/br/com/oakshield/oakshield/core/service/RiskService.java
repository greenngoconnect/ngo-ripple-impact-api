package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.risk.Risk;

import java.util.List;
import java.util.UUID;

public interface RiskService {
    Risk create(Risk risk);

    List<Risk> findAll();

    Risk update(UUID id, Risk risk);

    Risk findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    List<Risk> findByAssetId(UUID categoryId);

    int countRisksByAsset();
}
