package com.example.SiteCercolaFioravante.JwtUtils.integration_test;

import com.example.SiteCercolaFioravante.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class JwtUtilsIntegrationTest {

    private static JwtUtils jwtUtils;

    @BeforeAll
    static void setUp(@Autowired JwtUtils jwtUtilsWired){
        jwtUtils= jwtUtilsWired;
    }

    @Test
    void passwordResetCycleTest(){
        String tokenId = UUID.randomUUID().toString();
        String subjectId = Long.toString(1L);
        String token = jwtUtils.createResetPasswordToken(tokenId,subjectId);
        String[] info = jwtUtils.passwordResetJwtVerification(token);

        Assertions.assertEquals(tokenId,info[1]);
        Assertions.assertEquals(subjectId,info[0]);
    }

    @Test
    void refreshTokenCycleTest(){
        String tokenId = UUID.randomUUID().toString();
        String subjectId = Long.toString(1L);
        String token = jwtUtils.createRefreshToken(tokenId,subjectId);
        String[] info = jwtUtils.refreshTokenVerification(token);

        Assertions.assertEquals(tokenId,info[1]);
        Assertions.assertEquals(subjectId,info[0]);
    }

    @Test
    void accessTokenCycleTest(){
        String subjectId = Long.toString(1L);
        String role = "ROLE";
        String token = jwtUtils.createAccessToken(subjectId,role);
        String[] info = jwtUtils.getTokenAccessId(token);

        Assertions.assertEquals(subjectId,info[0]);
        Assertions.assertEquals(role,info[1]);
    }
}
