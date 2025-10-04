package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.consent.Consent;
import br.com.oakshield.oakshield.core.service.ConsentService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConsentServiceImpl implements ConsentService {

    private final ConsentRepository repository;

    public ConsentServiceImpl(ConsentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Consent create(Consent consent) {
        if (consent.getDataSubject().getId() == null) {
            throw new ResourceNotFoundException("Tipo de Consetimento não pode ser nulo.");
        }

        validCreateIfNotExists(consent.getDataSubject().getName());

        return repository.save(consent);
    }

    @Override
    public List<Consent> findAll() {
        return repository.findAll();
    }

    @Override
    public Consent update(UUID id, Consent consent) {
        boolean consentExists = repository.existsById(id);
        if (!consentExists) {
            throw new ResourceNotFoundException("Consetimento não encontrado.");
        }
        consent.setId(id);
        return repository.save(consent);
    }

    @Override
    public Consent findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consetimento não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Consetimento não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByDataSubjectName(name)
                .ifPresent(existingConsent -> {
                    throw new ResourceFoundException("Data Subject com o nome '" + name + "' já existe.");
                });
    }
}
