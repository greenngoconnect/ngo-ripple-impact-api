package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.commons.DateConstants;
import br.com.greenngoconnect.rippleimpact.core.domain.user.*;
import br.com.greenngoconnect.rippleimpact.core.repository.TokenRepository;
import br.com.greenngoconnect.rippleimpact.core.service.AuthenticationService;
import br.com.greenngoconnect.rippleimpact.core.service.JwtService;
import br.com.greenngoconnect.rippleimpact.core.service.UserService;
import br.com.greenngoconnect.rippleimpact.exception.EmailFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public User register(User user) throws Exception {
        var savedUser = savedUser(user);
        log.info("Usuário {} criado: " + savedUser);
        return savedUser;
    }

    private User savedUser(User user) {
        String email = user.getEmail();
        Optional<User> emailInUse = userService.findByEmail(email);
        if (emailInUse.isPresent()) {
            log.error("Usuaãrio já cadastrados: " + user);
            throw new EmailFoundException("Email já cadastrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Objecto usuário {} criado para ser salvo: " + user);
        return userService.create(user);
    }

    @Override
    public User forgotPassword(String email) throws Exception {
        Optional<User> emailInUse = userService.findByEmail(email);
        if (emailInUse.isPresent()) {
            String rawPassword = DateConstants.RAW_PASSWORD;
            emailInUse.get().setPassword(passwordEncoder.encode(rawPassword));
            return userService.create(emailInUse.get());
        }
        return null;
    }

    @Override
    public User resetPassword(String email, String newPassword) throws Exception {
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceFoundException("Email já cadastrado");
        }
        User userOp = user.get();
        String password = userOp.getPassword();
        user.get().setPassword(passwordEncoder.encode(password));
        return userService.create(userOp);
    }

    @Override
    public AuthenticationDTO authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        var user = userService.findByEmail(email)
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        log.info("Token: " + jwtToken);

        return AuthenticationDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    @Override
    public void revokeRefreshToken(String token) {
        tokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
           // rt.setRevokedAt(Instant.now());
            tokenRepository.save(rt);
        });
    }

    @Override
    public User getUser(UUID id) {
        return userService.findById(id);
    }

    private AuthenticationDTO responseToken(User user) {
        String savedToken = saveToken(user);
        String refreshToken = saveRefreshToken(user);

        return AuthenticationDTO.builder()
                .accessToken(savedToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String saveToken(User user) {
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return jwtToken;
    }

    private String saveRefreshToken(User user) {
        return jwtService.generateRefreshToken(user);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public RefreshTokenDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        final String refreshToken;
        final String userEmail;

        refreshToken = refreshTokenDTO.getRefreshToken();
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userService.findByEmail(userEmail)
                    .orElseThrow();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                return RefreshTokenDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }

        return null;
    }

    @Override
    public User updateUser(UUID id, User userFounded) {
        if (!userService.existsById(id)) {
            return null; // or throw an exception
        }
        userFounded.setId(id);
        return userService.update(id, userFounded);
    }
}
