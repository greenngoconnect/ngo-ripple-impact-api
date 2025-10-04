package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.incident.Indicator;
import br.com.oakshield.oakshield.core.service.IndicatorService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class IndicatorServiceImpl implements IndicatorService {

    private final IndicatorRepository repository;

    public IndicatorServiceImpl(IndicatorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Indicator create(Indicator indicator) {
        if (indicator.getName() == null || indicator.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(indicator.getName());

        return repository.save(indicator);
    }

    @Override
    public List<Indicator> findAll() {
        return repository.findAll();
    }

    @Override
    public Indicator update(UUID id, Indicator indicator) {
        boolean indicatorExists = repository.existsById(id);
        if (!indicatorExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        indicator.setId(id);
        return repository.save(indicator);
    }

    @Override
    public Indicator findById(UUID id) {
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
                .ifPresent(existingIndicator -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
