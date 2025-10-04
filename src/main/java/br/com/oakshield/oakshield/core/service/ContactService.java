package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.contact.Contact;

import java.util.List;
import java.util.UUID;

public interface ContactService {
    Contact create(Contact contact);

    List<Contact> findAll();

    Contact update(UUID id, Contact contact);

    Contact findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
