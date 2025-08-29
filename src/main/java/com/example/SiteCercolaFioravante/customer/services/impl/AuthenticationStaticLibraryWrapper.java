package com.example.SiteCercolaFioravante.customer.services.impl;

import org.hibernate.annotations.Comment;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticationStaticLibraryWrapper {

    public UUID getUUID(){
        return UUID.randomUUID();
    }

    public boolean checkPassword(String passwordInsert,String passwordExpected){
       return BCrypt.checkpw(passwordInsert,passwordExpected);
    }

    public String setPassword(String password){
      return  BCrypt.hashpw(password,BCrypt.gensalt());
    }


}
