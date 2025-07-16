package com.example.SiteCercolaFioravante.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( AuthenticationException.class )
    @ResponseBody
    public ResponseEntity<Error> handleAuthenticationException(Exception ex) {

        Error e = new Error(HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed ");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
    }

    @ExceptionHandler( JWTVerificationException.class )
    @ResponseBody
    public ResponseEntity<Error> handleAuthenticationJwtException(Exception ex) {

        Error e = new Error(HttpStatus.UNAUTHORIZED.value(),
                "Token non valido");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
    }



    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatusCode status = ex.getStatusCode();
        String message = ex.getReason();
        Error errorMessage = new Error(status.value(), message);
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Error> handleDataAccessException(DataAccessException ex) {
        Error errorMessage = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Errore interno del server");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgumentException(IllegalArgumentException ex) {
        Error errorMessage = new Error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGenericException(Exception ex) {
        Error errorMessage = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Errore interno del server");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
