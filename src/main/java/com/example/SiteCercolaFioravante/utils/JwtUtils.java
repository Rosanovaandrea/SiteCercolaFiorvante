package com.example.SiteCercolaFioravante.utils;


public interface JwtUtils {

   String createResetPasswordToken(String tokenId , String subjectId);

   String createRefreshToken(String tokenId , String subjectId);

   String createAccessToken(String login,String role);

   String[] passwordResetJwtVerification(String token);

   String[] refreshTokenVerification(String token);

   String[] getTokenAccessId(String token);

}
