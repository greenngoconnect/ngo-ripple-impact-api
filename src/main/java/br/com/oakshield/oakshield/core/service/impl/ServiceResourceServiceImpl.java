package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.service.ServiceResource;
import br.com.oakshield.oakshield.core.service.ServiceResourceService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ServiceResourceServiceImpl implements ServiceResourceService {

    private final ServiceResourceRepository repository;

    public ServiceResourceServiceImpl(ServiceResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceResource create(ServiceResource serviceResource) {
        if (serviceResource.getServiceResourceType().getId() == null) {
            throw new ResourceNotFoundException("Tipo de ativo não pode ser nulo.");
        }

        if (serviceResource.getName() == null || serviceResource.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(serviceResource.getName());

        return repository.save(serviceResource);
    }

    @Override
    public List<ServiceResource> findAll() {
        return repository.findAll();
    }

    @Override
    public ServiceResource update(UUID id, ServiceResource serviceResource) {
        boolean serviceExists = repository.existsById(id);
        if (!serviceExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        serviceResource.setId(id);
        return repository.save(serviceResource);
    }

    @Override
    public ServiceResource findById(UUID id) {
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
                .ifPresent(existingServiceResource -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
