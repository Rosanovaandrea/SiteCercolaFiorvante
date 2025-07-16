package com.example.SiteCercolaFioravante.customer.controller;

import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoComplete;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.LoginDto;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.PasswordResetDto;
import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping( "/authentication" )
@RequiredArgsConstructor
public class AuthenticationController {

    private final CustomerAuthenticationService service;
    private  static final String REFRESH_TOKEN ="refreshToken";

    @PostMapping( value = { "/email_password_reset" } )
    public ResponseEntity<Boolean> doEmailPasswordReset( @Valid @RequestBody String email ){
        service.doEmailPasswordReset( email );
        return new ResponseEntity<>(true, HttpStatus.OK );
    }

    @PostMapping( value = { "/password_reset" } )
    public ResponseEntity<Boolean> doPasswordReset( @Valid @RequestBody PasswordResetDto passwordResetDto) {
         service.doPasswordReset(  passwordResetDto.token(), passwordResetDto.password() );
        return new ResponseEntity<>(true, HttpStatus.OK );
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        Optional<Cookie> refrehTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
                .findFirst();

        return refrehTokenCookie.map(Cookie::getValue).orElse(null);


    }

    private void addAuthCookies(HttpServletResponse response, String refreshToken) {
        // Access Token Cookie
        Cookie accessTokenCookie = new Cookie(REFRESH_TOKEN, refreshToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true); // Imposta a false per HTTP su localhost se necessario per il test
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((30_000_000 / 1000));
        response.addCookie(accessTokenCookie);


    }

    @PostMapping( value = { "/login" } )
    public ResponseEntity<String>  doLogin(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        String [] tokens = service.doLogin(loginDto.email(), loginDto.password());
        addAuthCookies(response,tokens[0]);
        return new ResponseEntity<>( tokens[1], HttpStatus.OK );
    }

    @PostMapping( value = { "/registration" } )
    public ResponseEntity<String> doRegistration( @Valid @RequestBody CustomerDtoComplete customer, HttpServletResponse response) {
        String [] tokens = service.doRegistration(customer);
        addAuthCookies(response,tokens[0]);
        return new ResponseEntity<>( tokens[1] , HttpStatus.OK );
    }

    @PostMapping( value = { "/refreshToken" } )
    public ResponseEntity<String> doRefreshToken(HttpServletRequest httpServletRequest) {

        String token = extractRefreshTokenFromCookie(httpServletRequest);
        token = service.doRefreshAccessToken(token);

        return new ResponseEntity<>( token , HttpStatus.OK );
    }

}
