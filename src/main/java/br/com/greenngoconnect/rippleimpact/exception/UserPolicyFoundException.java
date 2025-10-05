package br.com.greenngoconnect.rippleimpact.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Usuário já confirmou essa política")
public class UserPolicyFoundException extends RuntimeException  {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Resource found exception.
     *
     * @param message the message
     */
    public UserPolicyFoundException(final String message) {
        super(message);
    }
}
