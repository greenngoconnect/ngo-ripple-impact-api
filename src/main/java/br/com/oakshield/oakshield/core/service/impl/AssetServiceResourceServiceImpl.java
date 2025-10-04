package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.asset.Asset;
import br.com.oakshield.oakshield.core.domain.asset.AssetServiceResource;
import br.com.oakshield.oakshield.core.domain.service.ServiceResource;
import br.com.oakshield.oakshield.core.service.AssetService;
import br.com.oakshield.oakshield.core.service.AssetServiceResourceService;
import br.com.oakshield.oakshield.core.service.ServiceResourceService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AssetServiceResourceServiceImpl implements AssetServiceResourceService {

    private final AssetServiceResourceRepository repository;
    private final AssetService assetService;
    private final ServiceResourceService serviceResourceService;

    public AssetServiceResourceServiceImpl(AssetServiceResourceRepository repository, AssetService assetService, ServiceResourceService serviceResourceService) {
        this.repository = repository;
        this.assetService = assetService;
        this.serviceResourceService = serviceResourceService;
    }

    @Override
    public List<AssetServiceResource> findAll() {
        return repository.findAll();
    }

    @Override
    public AssetServiceResource create(AssetServiceResource assetServiceResource) {
        UUID assetId = assetServiceResource.getAsset().getId();
        UUID serviceResourceId = assetServiceResource.getServiceResource().getId();
        if (assetId == null || serviceResourceId == null) {
            throw new ResourceNotFoundException("Ids title cannot be null or empty.");
        }

        // Cria e seta o ID composto
        AssetServiceResource.AssetServiceResourceId id = new AssetServiceResource.AssetServiceResourceId(assetId, serviceResourceId);
        assetServiceResource.setId(id);

        Asset assetFounded = assetService.findById(assetId);
        ServiceResource serviceResourceFounded = serviceResourceService.findById(serviceResourceId);
        assetServiceResource.setAsset(assetFounded);
        assetServiceResource.setServiceResource(serviceResourceFounded);
        assetServiceResource.setCreatedAt(OffsetDateTime.now());

        return repository.save(assetServiceResource);
    }

    @Override
    public AssetServiceResource findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentDataSubject not found."));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
