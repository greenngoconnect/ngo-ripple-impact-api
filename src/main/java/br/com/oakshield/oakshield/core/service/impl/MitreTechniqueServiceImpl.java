package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.mitretechnique.MitreTechnique;
import br.com.oakshield.oakshield.core.service.MitreTechniqueService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MitreTechniqueServiceImpl implements MitreTechniqueService {

    private final MitreTechniqueRepository repository;

    public MitreTechniqueServiceImpl(MitreTechniqueRepository repository) {
        this.repository = repository;
    }

    @Override
    public MitreTechnique create(MitreTechnique mitreTechnique) {
        if (mitreTechnique.getName() == null || mitreTechnique.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(mitreTechnique.getName());

        return repository.save(mitreTechnique);
    }

    @Override
    public List<MitreTechnique> findAll() {
        return repository.findAll();
    }

    @Override
    public MitreTechnique update(UUID id, MitreTechnique mitreTechnique) {
        boolean mitreTechniqueExists = repository.existsById(id);
        if (!mitreTechniqueExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        mitreTechnique.setId(id);
        return repository.save(mitreTechnique);
    }

    @Override
    public MitreTechnique findById(UUID id) {
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
                .ifPresent(existingMitreTechnique -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
