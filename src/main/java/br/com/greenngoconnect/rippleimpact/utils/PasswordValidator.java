package br.com.greenngoconnect.rippleimpact.utils;

import br.com.greenngoconnect.rippleimpact.commons.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordValidator implements ConstraintValidator<StrongPassword, String> {


    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[^a-zA-Z0-9]");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }
        if (!UPPERCASE.matcher(password).find()) {
            return false;
        }
        if (!LOWERCASE.matcher(password).find()) {
            return false;
        }
        if (!DIGIT.matcher(password).find()) {
            return false;
        }
        if (!SPECIAL.matcher(password).find()) {
            return false;
        }
        return true;
    }
}
