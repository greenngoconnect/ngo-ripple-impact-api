package br.com.oakshield.oakshield.api.mapper;

import br.com.oakshield.oakshield.api.dto.request.DepartmentRequest;
import br.com.oakshield.oakshield.core.domain.department.Department;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    Department from(DepartmentRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    DepartmentResponse to(Department department);

    List<DepartmentResponse> map(List<Department> departments);
}
