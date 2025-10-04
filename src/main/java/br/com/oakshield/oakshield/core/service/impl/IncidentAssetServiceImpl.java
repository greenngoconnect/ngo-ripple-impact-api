package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.asset.Asset;
import br.com.oakshield.oakshield.core.domain.asset.AssetServiceResource;
import br.com.oakshield.oakshield.core.domain.incident.Incident;
import br.com.oakshield.oakshield.core.domain.incident.IncidentAsset;
import br.com.oakshield.oakshield.core.service.AssetService;
import br.com.oakshield.oakshield.core.service.IncidentAssetService;
import br.com.oakshield.oakshield.core.service.IncidentService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentAssetServiceImpl implements IncidentAssetService {

    private final IncidentAssetRepository repository;
    private final IncidentService incidentService;
    private final AssetService assetService;

    public IncidentAssetServiceImpl(IncidentAssetRepository repository, IncidentService incidentService, AssetService assetService) {
        this.repository = repository;
        this.incidentService = incidentService;
        this.assetService = assetService;
    }

    @Override
    public List<IncidentAsset> findAll() {
        return repository.findAll();
    }

    @Override
    public IncidentAsset create(IncidentAsset incidentAsset) {
        UUID incidentId = incidentAsset.getIncident().getId();
        UUID assetId = incidentAsset.getAsset().getId();
        if (incidentId == null || assetId == null) {
            throw new ResourceNotFoundException("Ids title cannot be null or empty.");
        }

        // Cria e seta o ID composto
        IncidentAsset.IncidentAssetId id = new IncidentAsset.IncidentAssetId(incidentId, assetId);
        incidentAsset.setId(id);

        Incident incidentFounded = incidentService.findById(incidentId);
        incidentAsset.setIncident(incidentFounded);
        Asset assetFounded = assetService.findById(assetId);
        incidentAsset.setAsset(assetFounded);

        return repository.save(incidentAsset);
    }

    @Override
    public IncidentAsset findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentAsset not found."));
    }

    @Override
    public void deleteById(UUID id) {

        repository.deleteById(id);
    }
}
