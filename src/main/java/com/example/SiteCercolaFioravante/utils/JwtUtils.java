package com.example.SiteCercolaFioravante.utils;



import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Service
public class JwtUtils {

    private final String secretKey;

    public JwtUtils(
                    @Value("${security.jwt.token.secret-key}") String secretKey
                    ){
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login) {
        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime validity = now.plusMinutes(30);
        return JWT.create().withIssuer(login)
                .withIssuedAt(Instant.from(now))
                .withExpiresAt(Instant.from(validity))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getTokenEmail(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getIssuer();
    }

    public LocalDateTime getTokenLocalDate(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
       DecodedJWT decoded = verifier.verify(token);
        ZoneId systemZone = ZoneId.systemDefault();
       return decoded.getIssuedAt().toInstant().atZone(systemZone).toLocalDateTime();
    }
}

