package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.personaldatainventory.PersonalDataInventory;

import java.util.List;
import java.util.UUID;

public interface PersonalDataInventoryService {
    PersonalDataInventory create(PersonalDataInventory personalDataInventory);

    List<PersonalDataInventory> findAll();

    PersonalDataInventory update(UUID id, PersonalDataInventory personalDataInventory);

    PersonalDataInventory findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
