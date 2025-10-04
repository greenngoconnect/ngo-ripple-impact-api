package br.com.oakshield.oakshield.api.controller;

import br.com.oakshield.oakshield.api.dto.request.*;
import br.com.oakshield.oakshield.api.dto.response.AuthenticationResponse;
import br.com.oakshield.oakshield.api.dto.response.RefreshTokenResponse;
import br.com.oakshield.oakshield.api.dto.response.RegisterResponse;
import br.com.oakshield.oakshield.api.dto.response.UserConsent;
import br.com.oakshield.oakshield.api.mapper.AuthenticationMapper;
import br.com.oakshield.oakshield.core.domain.user.AuthenticationDTO;
import br.com.oakshield.oakshield.core.domain.user.RefreshTokenDTO;
import br.com.oakshield.oakshield.core.domain.user.Role;
import br.com.oakshield.oakshield.core.domain.user.User;
import br.com.oakshield.oakshield.core.service.AuthenticationService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.utils.RestUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationService service;
    private final AuthenticationMapper authenticationMapper;

    @PostMapping("/register-admin")
    public ResponseEntity<RegisterResponse> registerAdmin(@RequestBody RegisterRequest request) throws Exception {
        User user = authenticationMapper.from(request);
        if (request.getRole() == null || !request.getRole().equals(Role.ADMIN.name())) {
            user.setRole(Role.ADMIN);
        }
        User registered = service.register(user);
        RegisterResponse registerResponse = authenticationMapper.to(registered);

        log.info("User registered {] id: {} - email: {} - Perfil: {}", registered.getId(), registered.getEmail(), registered.getRole());
        URI location = RestUtils.getUri(registered.getId());

        return ResponseEntity.created(location).body(registerResponse);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) throws Exception {
        User user = authenticationMapper.from(request);
        if (request.getRole() == null || request.getRole().equals(Role.ADMIN.name())) {
            throw new ResourceFoundException("Não é permitido registrar usuário com perfil ADMIN");
        }
        User registered = service.register(user);
        RegisterResponse registerResponse = authenticationMapper.to(registered);

        log.info("User registered {] id: {} - email: {} - Perfil: {}", registered.getId(), registered.getEmail(), registered.getRole());
        URI location = RestUtils.getUri(registered.getId());

        return ResponseEntity.created(location).body(registerResponse);
    }

    @Operation(summary = "Update a ResidentDetails by Id", tags = {"associateDetailss", "put"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @PutMapping("/reset-password")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) throws Exception {
        var user = service.resetPassword(request.getEmail(), request.getPassword());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        RegisterResponse registerResponse = authenticationMapper.to(user);
        log.info("User password reset: id: {} - email: {}", user.getId());
        return ResponseEntity.ok(registerResponse);
    }

    @Operation(summary = "Update a ResidentDetails by Id", tags = {"associateDetailss", "put"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @PutMapping("/forgot-password")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws Exception {
        var user = service.forgotPassword(request.getEmail());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        RegisterResponse registerResponse = authenticationMapper.to(user);
        log.info("User password reset: id: {}", user.getEmail());
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1) Ler refresh_token de onde você envia (Cookie httpOnly ou Header/Body)
        String refreshToken = null;

        // Se usa cookie httpOnly:
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("refresh_token".equals(c.getName())) {
                    refreshToken = c.getValue();
                }
            }
        }

        // (ou) Se você envia no header:
        if (refreshToken == null) {
            refreshToken = request.getHeader("X-Refresh-Token");
        }

        // (ou) Se você envia no body (opcional):
        // if (refreshToken == null) { refreshToken = ... }

        // 2) Revoga no servidor (DB/Redis)
        if (refreshToken != null) {
            service.revokeRefreshToken(refreshToken);
        }

        // 3) Expira cookies no client (especialmente o refresh)
        Cookie expiredRefresh = new Cookie("refresh_token", "");
        expiredRefresh.setHttpOnly(true);
        expiredRefresh.setPath("/");
        expiredRefresh.setMaxAge(0);
        expiredRefresh.setSecure(true); // em prod/https
        response.addCookie(expiredRefresh);

        // Se você também setou um access_token httpOnly pelo backend, expire-o:
        Cookie expiredAccess = new Cookie("access_token", "");
        expiredAccess.setHttpOnly(true);
        expiredAccess.setPath("/");
        expiredAccess.setMaxAge(0);
        expiredAccess.setSecure(true);
        response.addCookie(expiredAccess);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationDTO authenticate = service.authenticate(request.getEmail(), request.getPassword());
        log.info("User logged: " + authenticate.getUserId() + " - " + authenticate.getEmail() + " - " + authenticate.getRole());
        AuthenticationResponse response = authenticationMapper.toResponse(authenticate);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve a User by Id",
            description = "Get a Driver object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"associates", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RegisterResponse> findUserById(@PathVariable("id") UUID id) {
        User userFounded = service.getUser(id);

        if (userFounded == null) {
            return ResponseEntity.noContent().build();
        }

        RegisterResponse userResponse = authenticationMapper.to(userFounded);
        log.info("User found: id: {} - email: {}", userFounded.getId(), userFounded.getEmail());
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Retrieve a User by Id",
            description = "Get a Driver object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"associates", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/user/{id}/consent/accept")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserConsent> findUserByIdConsentAccept(@PathVariable("id") UUID id) {
        User userFounded = service.getUser(id);

        if (userFounded == null) {
            return ResponseEntity.noContent().build();
        }

        User updateUserWithConsent = service.updateUser(id, userFounded);

        UserConsent userConsentBuilder = UserConsent.builder().consentAccepted(updateUserWithConsent.isConsentAccepted()).build();
        log.info("User Consent:", userConsentBuilder.isConsentAccepted());
        return ResponseEntity.ok(userConsentBuilder);
    }

    @Operation(
            summary = "User consent accept",
            description = "User consent accept",
            tags = {"associates", "put"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PutMapping("/user/{id}/consent/accept")
    public ResponseEntity<Boolean> consentAccept(@PathVariable("id") UUID id) throws Exception {
        User userFounded = service.getUser(id);

        if (userFounded == null) {
            return ResponseEntity.noContent().build();
        }

        userFounded.setConsentAccepted(true);
        service.updateUser(id, userFounded);

        log.info("User consent accepted: id: {} - email: {}", userFounded.getId(), userFounded.getEmail());
        return ResponseEntity.ok(true);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenDTO refreshToken = authenticationMapper.toRefreshTokenToDTO(request);
        log.info("Refreshing token for user: {}", refreshToken.getRefreshToken());

        RefreshTokenDTO refreshedToken = service.refreshToken(refreshToken);
        if (refreshedToken == null) {
            log.warn("Failed to refresh token for user: {}", refreshToken.getRefreshToken());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Token refreshed successfully for user: {}", refreshedToken.getRefreshToken());
        RefreshTokenResponse response = authenticationMapper.toRefreshTokenToResponse(refreshedToken);
        log.info("Returning refreshed token: {}", response.getAccessToken());
        return ResponseEntity.ok(response);
    }
}
