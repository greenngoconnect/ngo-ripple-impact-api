package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.Indicator;

import java.util.List;
import java.util.UUID;

public interface IndicatorService {
    Indicator create(Indicator asset);

    List<Indicator> findAll();

    Indicator update(UUID id, Indicator asset);

    Indicator findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
