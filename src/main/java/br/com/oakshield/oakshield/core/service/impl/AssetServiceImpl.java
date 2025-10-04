package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.asset.Asset;
import br.com.oakshield.oakshield.core.service.AssetService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository repository;
    private final RiskSuggestionServiceImpl riskSuggestionServiceImpl;

    public AssetServiceImpl(AssetRepository repository, RiskSuggestionServiceImpl riskSuggestionServiceImpl) {
        this.repository = repository;
        this.riskSuggestionServiceImpl = riskSuggestionServiceImpl;
    }

    @Override
    public Asset create(Asset asset) {
        if (asset.getAssetType().getId() == null) {
            throw new ResourceNotFoundException("Tipo de ativo não pode ser nulo.");
        }

        if (asset.getName() == null || asset.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(asset.getName());

        Asset assetSaved = repository.save(asset);
        boolean suggestRisksForAssetSaved = riskSuggestionServiceImpl.suggestRisksForAsset(asset);
        if (!suggestRisksForAssetSaved) {
            log.info("Não foi possível sugerir riscos para o ativo: {}", asset.getName());
        }
        return assetSaved;
    }

    @Override
    public List<Asset> findAll() {
        return repository.findAll();
    }

    @Override
    public Asset update(UUID id, Asset asset) {
        boolean assetExists = repository.existsById(id);
        if (!assetExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        asset.setId(id);
        return repository.save(asset);
    }

    @Override
    public Asset findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ativo não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Ativo não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByName(name)
                .ifPresent(existingAsset -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
