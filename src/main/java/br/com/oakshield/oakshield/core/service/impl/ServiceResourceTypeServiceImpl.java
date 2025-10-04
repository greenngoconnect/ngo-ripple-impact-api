package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.service.ServiceResourceType;
import br.com.oakshield.oakshield.core.service.ServiceResourceTypeService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceResourceTypeServiceImpl implements ServiceResourceTypeService {

    private final ServiceResourceTypeRepository repository;

    public ServiceResourceTypeServiceImpl(ServiceResourceTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceResourceType create(ServiceResourceType serviceResourceType) {
        if (serviceResourceType.getName() == null || serviceResourceType.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do tipo de ativo não pode ser nulo ou vazio.");
        }
        validCreateIfNotExists(serviceResourceType.getName());
        return repository.save(serviceResourceType);
    }

    @Override
    public ServiceResourceType update(UUID id, ServiceResourceType serviceResourceType) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de ativo não encontrado.");
        }
        serviceResourceType.setId(id); // Ensure the ID is set for the update
        return repository.save(serviceResourceType);
    }

    @Override
    public List<ServiceResourceType> findAll() {
        return repository.findAll();
    }

    @Override
    public ServiceResourceType findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de ativo não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de ativo não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByName(name)
                .ifPresent(serviceResourceType -> {
                    throw new ResourceFoundException("Tipo de ativo já existe com o nome: " + name);
                });
    }
}
