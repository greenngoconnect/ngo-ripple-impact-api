package br.com.greenngoconnect.rippleimpact.core.service;

import br.com.greenngoconnect.rippleimpact.core.domain.department.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    Department create(Department department);

    List<Department> findAll();

    Department update(UUID id, Department department);

    Department findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
