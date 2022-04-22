package co.fullstacklabs.cuboid.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2021-10-22
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {
    public InternalServerException(final String message) {
        super(message);
    }
}
