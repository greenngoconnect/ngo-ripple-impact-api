package br.com.oakshield.oakshield.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Resource found exception.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email já cadastrado.")
public class EmailFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Resource found exception.
     *
     * @param message the message
     */
    public EmailFoundException(final String message) {
        super(message);
    }

}