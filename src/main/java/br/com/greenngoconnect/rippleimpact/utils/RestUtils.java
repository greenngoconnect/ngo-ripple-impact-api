package br.com.greenngoconnect.rippleimpact.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RestUtils {

    public static URI getUri(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    public static String getAppUrl() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("Requisição fora de contexto HTTP");
        }

        HttpServletRequest request = attrs.getRequest();
        return request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
    }
}