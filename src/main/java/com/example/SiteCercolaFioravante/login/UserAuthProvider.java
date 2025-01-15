package com.example.SiteCercolaFioravante.login;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key}")
    public String secretKey;

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

    public Authentication validateToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
        JWTverifier = JWT.require(Algorithm.HMAC256(secretKey))
                .build();
       Decoded decoded = verifier.verify(token);
       decoded
    }
}

