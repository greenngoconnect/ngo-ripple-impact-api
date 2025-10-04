package br.com.oakshield.oakshield.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneratedCode {

    public static String generateProductCode() {
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return code + System.currentTimeMillis();
    }
}
