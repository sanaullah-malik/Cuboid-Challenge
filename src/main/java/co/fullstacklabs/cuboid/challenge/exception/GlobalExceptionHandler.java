package co.fullstacklabs.cuboid.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDetails resourceNotFoundException(final ResourceNotFoundException ex, final WebRequest request) {
        return new ErrorDetails(ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = {UnprocessableEntityException.class})
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorDetails unprocessedEntityException(final UnprocessableEntityException ex, final WebRequest request) {
        return new ErrorDetails(ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = {InternalServerException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetails internalServerException(final InternalServerException ex, final WebRequest request) {
        return new ErrorDetails(ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError constraintViolationException(final ConstraintViolationException ex) {
        final ValidationError errors = new ValidationError();
        for (final ConstraintViolation violation : ex.getConstraintViolations()) {
            errors.addViolations(new ErrorDetails(violation.getMessage(), violation.getPropertyPath().toString()));
        }
        return errors;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError constraintViolationException(final MethodArgumentNotValidException ex) {
        final ValidationError errors = new ValidationError();
        for (final FieldError violation : ex.getBindingResult().getFieldErrors()) {
            errors.addViolations(new ErrorDetails(violation.getDefaultMessage(), violation.getField()));
        }
        return errors;
    }
}
