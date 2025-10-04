package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.user.User;
import br.com.oakshield.oakshield.core.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshExpiration;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractUserId(String token) {
        return extractClaim(token, c -> c.get("user_id", String.class)); // UUID
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return jwtParser().parseClaimsJws(token).getBody();
    }

    /* ===================== Geração de tokens ===================== */

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (token == null || userDetails == null) return false;
        if (isTokenExpired(token)) return false;

        // Pega claims do token
        final Claims claims = extractAllClaims(token);

        // sub = UUID do usuário (id)
        final String tokenUserId = claims.getSubject();

        // email pode (ou não) estar no token
        final String tokenEmail = claims.get("email", String.class);

        // Usuário concreto
        final User user = (User) userDetails;

        // 1) ID deve bater (regra principal)
        final boolean idMatch = user.getId() != null
                && user.getId().toString().equals(tokenUserId);

        // 2) Se houver email no token, também deve bater (regra adicional, defensiva)
        final boolean emailMatch = (tokenEmail == null)
                || (user.getEmail() != null && user.getEmail().equalsIgnoreCase(tokenEmail));

        return idMatch && emailMatch;
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        final User user = (User) userDetails;

        if (extraClaims == null) {
            extraClaims = new HashMap<>();
        }

        // claims úteis para o front/back
        extraClaims.putIfAbsent("user_id", user.getId().toString());
        extraClaims.putIfAbsent("email", user.getEmail());
        extraClaims.putIfAbsent("name", user.getName());
        if (user.getRole() != null) {
            extraClaims.putIfAbsent("role", user.getRole().name());
        }
        extraClaims.putIfAbsent("consent", user.isConsentAccepted());

        final long now = System.currentTimeMillis();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId().toString()) // <<< sub = UUID (id)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /* ===================== Chave/Parser ===================== */

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSigningKey() {
        try {
            // Se a secret estiver em Base64 (recomendado)
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            // fallback: secret como texto puro (garanta >= 32 bytes p/ HS256)
            byte[] raw = secretKey.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(raw);
        }
    }

    private JwtParser jwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(60) // tolerância de 60s
                .build();
    }
}
