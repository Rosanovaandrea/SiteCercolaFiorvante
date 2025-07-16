package com.example.SiteCercolaFioravante.utils;



import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.SiteCercolaFioravante.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;

@Service
public class JwtUtils {

    private static final String TOKEN_REFRESH_ID = "RefreshTokenId";
    private static final String TOKEN_TYPE = "TokenType";
    private static final String TIME_ZONE = "Europe/Rome";
    private static final String ROLE = "role";
    private final String secretKey;
    private final  String issuer;


    public JwtUtils(
                    @Value("${security.jwt.token.secret-key}") String secretKey,
                    @Value("${security.jwt.token.issuer}") String issuer
                    ){
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

    public String createRefreshOrPasswordResetToken(String tokenId , String id, TokenType type) {
        LocalDateTime now = LocalDateTime.now() ;

        ZoneId zoneId = ZoneId.of(TIME_ZONE);
        ZonedDateTime nowZone = ZonedDateTime.of(now,zoneId);
        ZonedDateTime validityZone = ZonedDateTime.of(now.plusHours(5),zoneId);

        return JWT.create().withSubject(id).withIssuer(issuer).withClaim(TOKEN_REFRESH_ID,tokenId).withClaim(TOKEN_TYPE,type.toString())
                .withIssuedAt(Instant.from(nowZone))
                .withExpiresAt(Instant.from(validityZone))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String createAccessToken(String login,String role) {
        LocalDateTime now = LocalDateTime.now() ;

        ZoneId zoneId = ZoneId.of(TIME_ZONE);
        ZonedDateTime nowZone = ZonedDateTime.of(now,zoneId);
        ZonedDateTime validityZone = ZonedDateTime.of(now.plusMinutes(5),zoneId);

        return JWT.create().withSubject(login).withIssuer(issuer).withClaim(ROLE,role)
                .withIssuedAt(Instant.from(nowZone))
                .withExpiresAt(Instant.from(validityZone))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getTokenRefreshOrPasswordResetInfo(String token){

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).withIssuer(issuer).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject()+" "+decoded.getClaim(TOKEN_REFRESH_ID).asString()+" "+decoded.getClaim(TOKEN_TYPE).asString();
    }


    public String[] getTokenAccessId(String token){

            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).withIssuer(issuer).build();
            DecodedJWT decoded = verifier.verify(token);
            return new String[] {decoded.getSubject(),decoded.getClaim(ROLE).asString()};
    }
}

