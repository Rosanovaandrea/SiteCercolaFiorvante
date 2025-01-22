package com.example.SiteCercolaFioravante.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatusCode status = ex.getStatusCode();
        String message = ex.getReason();
        Error errorMessage = new Error(status.value(), message);
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(DataAccessException.class) // Gestione specifica per eccezioni di accesso al database
    public ResponseEntity<Error> handleDataAccessException(DataAccessException ex) {
        System.err.println("Errore accesso al database: " + ex.getMessage());
        ex.printStackTrace(); // Utile per il debugging
        Error errorMessage = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Errore interno del server (Database)");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class) // Gestione di eccezioni generiche (da usare con cautela)
    public ResponseEntity<Error> handleGenericException(Exception ex) {
        System.err.println("Eccezione non gestita: " + ex.getMessage());
        ex.printStackTrace();
        Error errorMessage = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Errore interno del server");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
