package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.asset.AssetTag;
import br.com.oakshield.oakshield.core.service.AssetTagService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AssetTagServiceImpl implements AssetTagService {

    private final AssetTagRepository repository;

    public AssetTagServiceImpl(AssetTagRepository repository) {
        this.repository = repository;
    }

    @Override
    public AssetTag create(AssetTag assetTag) {
        if (assetTag.getName() == null || assetTag.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(assetTag.getName());

        return repository.save(assetTag);
    }

    @Override
    public List<AssetTag> findAll() {
        return repository.findAll();
    }

    @Override
    public AssetTag update(UUID id, AssetTag assetTag) {
        boolean assetTagExists = repository.existsById(id);
        if (!assetTagExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        assetTag.setId(id);
        return repository.save(assetTag);
    }

    @Override
    public AssetTag findById(UUID id) {
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
                .ifPresent(existingAssetTag -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
