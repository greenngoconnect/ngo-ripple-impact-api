package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.contact.Contact;
import br.com.oakshield.oakshield.core.service.ContactService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Contact create(Contact contact) {
        if (contact.getFullName() == null || contact.getFullName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(contact.getFullName());

        return repository.save(contact);
    }

    @Override
    public List<Contact> findAll() {
        return repository.findAll();
    }

    @Override
    public Contact update(UUID id, Contact contact) {
        boolean contactExists = repository.existsById(id);
        if (!contactExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        contact.setId(id);
        return repository.save(contact);
    }

    @Override
    public Contact findById(UUID id) {
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

    private void validCreateIfNotExists(String fullName) {
        repository.findByFullName(fullName)
                .ifPresent(existingContact -> {
                    throw new ResourceFoundException("Ativo com o nome '" + fullName + "' já existe.");
                });
    }
}
