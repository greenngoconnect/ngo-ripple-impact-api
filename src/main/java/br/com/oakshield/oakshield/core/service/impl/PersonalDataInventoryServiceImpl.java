package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.personaldatainventory.PersonalDataInventory;
import br.com.oakshield.oakshield.core.service.PersonalDataInventoryService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PersonalDataInventoryServiceImpl implements PersonalDataInventoryService {

    private final PersonalDataInventoryRepository repository;

    public PersonalDataInventoryServiceImpl(PersonalDataInventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public PersonalDataInventory create(PersonalDataInventory personalDataInventory) {
        if (personalDataInventory.getDataSubject().getId() == null) {
            throw new ResourceNotFoundException("PersonalDataInventory com essa descrição já existe.");
        }
        validCreateIfNotExists(personalDataInventory.getAccessScope());
        return repository.save(personalDataInventory);
    }

    @Override
    public List<PersonalDataInventory> findAll() {
        return repository.findAll();
    }

    @Override
    public PersonalDataInventory findById(UUID personalDataInventoryId) {
        return repository.findById(personalDataInventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Política não encontrada."));
    }

    @Override
    public PersonalDataInventory update(UUID id, PersonalDataInventory personalDataInventory) {
        if (!repository.existsById(id)) {
            return null; // or throw an exception
        }
        personalDataInventory.setId(id);
        return repository.save(personalDataInventory);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Política não encontrada.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String accessScope) {
        repository.findByAccessScope(accessScope)
                .ifPresent(assetType -> {
                    throw new ResourceFoundException(
                            "Política com o nome '" + accessScope + "' já existe.");
                });
    }
}
