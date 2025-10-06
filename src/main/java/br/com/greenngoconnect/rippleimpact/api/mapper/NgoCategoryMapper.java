package br.com.greenngoconnect.rippleimpact.api.mapper;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoCategoryRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoCategoryResponse;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NgoCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    NgoCategory from(NgoCategoryRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    NgoCategoryResponse to(NgoCategory ngoCategory);

    List<NgoCategoryResponse> map(List<NgoCategory> ngoCategories);
}
