package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.asset.AssetType;
import br.com.oakshield.oakshield.core.service.AssetTypeService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssetTypeServiceImpl implements AssetTypeService {

    private final AssetTypeRepository repository;

    public AssetTypeServiceImpl(AssetTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public AssetType create(AssetType assetType) {
        if (assetType.getName() == null || assetType.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do tipo de ativo não pode ser nulo ou vazio.");
        }
        validCreateIfNotExists(assetType.getName());
        return repository.save(assetType);
    }

    @Override
    public AssetType update(UUID id, AssetType assetType) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de ativo não encontrado.");
        }
        assetType.setId(id); // Ensure the ID is set for the update
        return repository.save(assetType);
    }

    @Override
    public List<AssetType> findAll() {
        return repository.findAll();
    }

    @Override
    public AssetType findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de ativo não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de ativo não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByName(name)
                .ifPresent(assetType -> {
                    throw new ResourceFoundException("Tipo de ativo já existe com o nome: " + name);
                });
    }
}
