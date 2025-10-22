package br.com.greenngoconnect.rippleimpact.api.mapper;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoResponse;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NgoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "ngoCategoryId", target = "ngoCategory.id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "fantasyName", target = "fantasyName")
    @Mapping(source = "companyId", target = "companyId")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "ngoStatus", target = "ngoStatus")
    Ngo from(NgoRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ngoCategoryId", source = "ngoCategory.id")
    NgoResponse to(Ngo ngo);

    List<NgoResponse> map(List<Ngo> ngos);
}
