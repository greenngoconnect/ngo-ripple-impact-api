package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoCategoryRepository;
import br.com.greenngoconnect.rippleimpact.core.service.NgoCategoryService;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NgoCategoryServiceImpl implements NgoCategoryService {

    private final NgoCategoryRepository repository;

    public NgoCategoryServiceImpl(NgoCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public NgoCategory create(NgoCategory ngoCategory) {
        if (ngoCategory.getName() == null || ngoCategory.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(ngoCategory.getName());

        return repository.save(ngoCategory);
    }

    @Override
    public List<NgoCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public NgoCategory update(UUID id, NgoCategory ngoCategory) {
        boolean ngoCategoryExists = repository.existsById(id);
        if (!ngoCategoryExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        ngoCategory.setId(id);
        return repository.save(ngoCategory);
    }

    @Override
    public NgoCategory findById(UUID id) {
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
                .ifPresent(existingNgoCategory -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
