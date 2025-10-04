package br.com.oakshield.oakshield.api.mapper;

import br.com.oakshield.oakshield.api.dto.request.UserRequest;
import br.com.oakshield.oakshield.api.dto.response.UserResponse;
import br.com.oakshield.oakshield.core.domain.user.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "tokens", ignore = true)
    User from(UserRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    UserResponse to(User user);

    List<UserResponse> map(List<User> users);
}
