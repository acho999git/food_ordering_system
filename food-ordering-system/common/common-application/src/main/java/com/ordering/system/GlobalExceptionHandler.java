package com.ordering.system;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
// the reason for this class is because we have to have global exception handling with 500 status
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto handleException(final ValidationException exception) {

        if (exception instanceof ConstraintViolationException) {
            final String validations = getViolations((ConstraintViolationException)exception);
            log.error(validations, exception);
            return ErrorDto.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .build();
        } else {
            log.error(exception.getMessage(), exception);
            final String validations = exception.getMessage();
            log.error(validations, exception);
            return ErrorDto.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .build();
        }
    }

    private  String getViolations(final ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
    }
}
