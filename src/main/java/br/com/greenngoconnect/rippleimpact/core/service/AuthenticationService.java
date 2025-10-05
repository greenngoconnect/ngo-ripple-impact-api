package br.com.greenngoconnect.rippleimpact.core.service;

import br.com.greenngoconnect.rippleimpact.core.domain.user.AuthenticationDTO;
import br.com.greenngoconnect.rippleimpact.core.domain.user.RefreshTokenDTO;
import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface AuthenticationService {
    User register(User user) throws Exception;

    User forgotPassword(String email) throws Exception;

    User resetPassword(String email, String newPassword) throws Exception;

    AuthenticationDTO authenticate(String email, String password);

    @Transactional
    void revokeRefreshToken(String token);

    User getUser(UUID id);

    RefreshTokenDTO refreshToken(RefreshTokenDTO refreshTokenDTO);

    User updateUser(UUID id, User userFounded);
}
