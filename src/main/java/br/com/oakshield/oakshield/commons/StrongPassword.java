package br.com.oakshield.oakshield.commons;

import br.com.oakshield.oakshield.utils.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default "Senha fraca: deve conter ao menos 8 caracteres, incluindo maiúsculas, minúsculas, número e símbolo.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
