package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.service.ServiceResource;

import java.util.List;
import java.util.UUID;

public interface ServiceResourceService {
    ServiceResource create(ServiceResource serviceResource);

    ServiceResource update(UUID id, ServiceResource serviceResource);

    List<ServiceResource> findAll();

    ServiceResource findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
