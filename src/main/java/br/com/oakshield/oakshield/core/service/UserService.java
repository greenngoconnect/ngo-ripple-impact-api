package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(User user);

    List<User> findAll();

    User findById(UUID userId);

    User update(UUID id, User user);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    Optional<User> findByEmail(String email);
}
