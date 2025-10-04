package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.IncidentAsset;

import java.util.List;
import java.util.UUID;

public interface IncidentAssetService {

    IncidentAsset create(IncidentAsset incidentAsset);

    List<IncidentAsset> findAll();

    IncidentAsset findById(UUID id);

    void deleteById(UUID id);

}
