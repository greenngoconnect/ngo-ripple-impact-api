package br.com.greenngoconnect.rippleimpact.api.mapper;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoSocialNetworkRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoSocialNetworkResponse;
import br.com.greenngoconnect.rippleimpact.core.domain.department.Department;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoSocialNetwork;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NgoSocialNetworkMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "ngoId", target = "ngo.id")
    @Mapping(source = "linkedin", target = "linkedin")
    @Mapping(source = "twitterX", target = "twitterX")
    @Mapping(source = "instagram", target = "instagram")
    @Mapping(source = "website", target = "website")
    NgoSocialNetwork from(NgoSocialNetworkRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ngoId", source = "ngo.id")
    NgoSocialNetworkResponse to(NgoSocialNetwork ngoSocialNetwork);

    List<NgoSocialNetworkResponse> map(List<NgoSocialNetwork> ngoSocialNetworks);
}
