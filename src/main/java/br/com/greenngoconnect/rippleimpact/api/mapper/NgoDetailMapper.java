package br.com.greenngoconnect.rippleimpact.api.mapper;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoDetailRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoDetailResponse;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoDetail;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NgoDetailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "ngoId", target = "ngo.id")
    @Mapping(source = "responsibleName", target = "responsibleName")
    @Mapping(source = "responsibleEmail", target = "responsibleEmail")
    @Mapping(source = "responsiblePhone", target = "responsiblePhone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "mission", target = "mission")
    @Mapping(source = "vision", target = "vision")
    @Mapping(source = "values", target = "values")
    NgoDetail from(NgoDetailRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(source = "ngo.id", target = "ngoId")
    NgoDetailResponse to(NgoDetail ngoDetail);

    List<NgoDetailResponse> map(List<NgoDetail> ngoDetails);
}
