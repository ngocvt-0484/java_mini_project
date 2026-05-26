package com.example.employeemanagement.exception;

import com.example.employeemanagement.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        ErrorResponse response =
                ErrorResponse.builder()
                        .message("Validation failed")
                        .errors(errors)
                        .build();

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex
    ) {

        ErrorResponse response = ErrorResponse.builder()
                .message("Not found resource")
                .errors(List.of(ex.getMessage()))
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex
    ) {

        ErrorResponse response = ErrorResponse.builder()
                .message("Duplicate resource")
                .errors(List.of(ex.getMessage()))
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}
