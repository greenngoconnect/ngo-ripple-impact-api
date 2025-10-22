package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoRepository;
import br.com.greenngoconnect.rippleimpact.core.service.NgoService;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NgoServiceImpl implements NgoService {

    private final NgoRepository repository;

    public NgoServiceImpl(NgoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ngo create(Ngo department) {
        if (department.getName() == null || department.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(department.getName());

        return repository.save(department);
    }

    @Override
    public List<Ngo> findAll() {
        return repository.findAll();
    }

    @Override
    public Ngo update(UUID id, Ngo department) {
        boolean departmentExists = repository.existsById(id);
        if (!departmentExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        department.setId(id);
        return repository.save(department);
    }

    @Override
    public Ngo findById(UUID id) {
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
                .ifPresent(existingNgo -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
