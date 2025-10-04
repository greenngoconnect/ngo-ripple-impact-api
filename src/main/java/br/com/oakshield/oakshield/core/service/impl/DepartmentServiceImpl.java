package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.department.Department;
import br.com.oakshield.oakshield.core.repository.DepartmentRepository;
import br.com.oakshield.oakshield.core.service.DepartmentService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department create(Department department) {
        if (department.getName() == null || department.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(department.getName());

        return repository.save(department);
    }

    @Override
    public List<Department> findAll() {
        return repository.findAll();
    }

    @Override
    public Department update(UUID id, Department department) {
        boolean departmentExists = repository.existsById(id);
        if (!departmentExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        department.setId(id);
        return repository.save(department);
    }

    @Override
    public Department findById(UUID id) {
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
                .ifPresent(existingDepartment -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
