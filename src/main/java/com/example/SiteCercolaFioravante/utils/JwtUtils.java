package com.example.SiteCercolaFioravante.utils;



import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;


@Service
@AllArgsConstructor
public class JwtUtils {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1_800_000);
        return JWT.create().withIssuer(login)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getTokenEmail(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getIssuer();
    }

    public Date getTokenDate(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
       DecodedJWT decoded = verifier.verify(token);
       return decoded.getIssuedAt();
    }
}

