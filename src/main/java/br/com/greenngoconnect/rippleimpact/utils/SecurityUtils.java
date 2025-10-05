package br.com.greenngoconnect.rippleimpact.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    /**
     * Retorna o nome (username/login) do usuário autenticado.
     * Se não houver usuário autenticado, retorna null.
     */
    public static String getCurrentUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return auth.getName(); // geralmente username ou subject (JWT)
    }
}

