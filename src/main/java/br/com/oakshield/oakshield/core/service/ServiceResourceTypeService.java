package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.service.ServiceResourceType;

import java.util.List;
import java.util.UUID;

public interface ServiceResourceTypeService {
    ServiceResourceType create(ServiceResourceType serviceResourceType);

    ServiceResourceType update(UUID id, ServiceResourceType serviceResourceType);

    List<ServiceResourceType> findAll();

    ServiceResourceType findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
