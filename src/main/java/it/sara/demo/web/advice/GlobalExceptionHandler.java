package it.sara.demo.web.advice;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.exception.GenericException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<StatusDTO> handleGenericException(GenericException ex) {
        return ResponseEntity.ok(ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request body");

        return ResponseEntity.ok(new StatusDTO(400, errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StatusDTO> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .findFirst()
                .orElse("Invalid request parameter");

        return ResponseEntity.ok(new StatusDTO(400, errorMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StatusDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Invalid value for parameter '%s'",
                ex.getName());

        return ResponseEntity.ok(new StatusDTO(400, errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StatusDTO> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.ok(new StatusDTO(400, "Malformed JSON request"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<StatusDTO> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.ok(new StatusDTO(400, "Missing required parameter: " + ex.getParameterName()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StatusDTO> handleBadCredentialsException(Exception ex) {
        return ResponseEntity.ok(new StatusDTO(401, "Invalid email or password"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusDTO> handleGeneric(Exception ex) {
        return ResponseEntity.ok(new StatusDTO(500, "Internal server error"));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<StatusDTO> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return ResponseEntity.ok(new StatusDTO(400, "Invalid request parameters"));
    }

}
