package br.com.greenngoconnect.rippleimpact.core.service;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;

import java.util.List;
import java.util.UUID;

public interface NgoService {
    Ngo create(Ngo ngo);

    List<Ngo> findAll();

    Ngo update(UUID id, Ngo ngo);

    Ngo findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
