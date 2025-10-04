package br.com.oakshield.oakshield.api.mapper;

import br.com.oakshield.oakshield.api.dto.request.RefreshTokenRequest;
import br.com.oakshield.oakshield.api.dto.request.RegisterRequest;
import br.com.oakshield.oakshield.api.dto.response.AuthenticationResponse;
import br.com.oakshield.oakshield.api.dto.response.RefreshTokenResponse;
import br.com.oakshield.oakshield.api.dto.response.RegisterResponse;
import br.com.oakshield.oakshield.api.dto.response.UserResponse;
import br.com.oakshield.oakshield.core.domain.user.AuthenticationDTO;
import br.com.oakshield.oakshield.core.domain.user.RefreshTokenDTO;
import br.com.oakshield.oakshield.core.domain.user.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "tokens", ignore = true)
    User from(RegisterRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "consentAccepted", source = "consentAccepted")
    @Mapping(target = "userId", source = "id")
    RegisterResponse to(User user);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "userId", source = "userId")
    AuthenticationResponse toResponse(AuthenticationDTO authenticationDTO);

    @Mapping(target = "refreshToken", source = "refreshToken")
    RefreshTokenDTO toRefreshTokenToDTO(RefreshTokenRequest refreshTokenRequest);

    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "accessToken", source = "accessToken")
    RefreshTokenResponse toRefreshTokenToResponse(RefreshTokenDTO refreshTokenDTO);

    List<UserResponse> map(List<User> users);
}
