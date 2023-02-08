package xyz.mpdn.resource.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public class MimeTypeValidationException extends ConstraintViolationException {
    public MimeTypeValidationException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }

    public MimeTypeValidationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }
}
