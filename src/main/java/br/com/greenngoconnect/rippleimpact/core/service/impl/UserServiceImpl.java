package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import br.com.greenngoconnect.rippleimpact.core.repository.UserRepository;
import br.com.greenngoconnect.rippleimpact.core.service.UserService;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ResourceNotFoundException("Nome do usuário não pode ser nulo ou vazio.");
        }
        validCreateIfNotExists(user.getName());
        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    @Override
    public User update(UUID id, User user) {
        if (!repository.existsById(id)) {
            return null; // or throw an exception
        }
        user.setId(id);
        return repository.save(user);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado.");
        }
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByName(name)
                .ifPresent(assetType -> {
                    throw new ResourceFoundException("Usuário com o nome '" + name + "' já existe.");
                });
    }
}
