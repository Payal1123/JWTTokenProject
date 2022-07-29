package com.example.JWTSpringBoot.JWTUtility;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    @Value("${app.secret}")
    private String secret;

    //6 Validate Username in token and database ,expdate

    public boolean ValidateToken(String token,String username)
    {
        String tokenUserName = getUSername(token);
        return (username.equals(tokenUserName) && !isTokenExp(token));

    }

    // 5 validate Exp date
     public  boolean isTokenExp(String token)
     {
          Date expDate = getExpDate(token);
          return  expDate.before(new Date(System.currentTimeMillis()));


     }

    //4 read subject/username
    public String getUSername(String token)
    {
        return  getclaims(token).getSubject();
    }

    //3 Read Exp Date
     public Date getExpDate(String token)
     {
         return getclaims(token).getExpiration();

     }

    //2 Read Claims
     public Claims getclaims(String token)
     {
          return Jwts.parser()
                  .setSigningKey(secret.getBytes())
                  .parseClaimsJws(token)
                  .getBody();
     }

  //1 generate token

    public String Generatetoken(String subject)
    {

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer("ShwetaNarwade")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(15)))
                .signWith(SignatureAlgorithm.HS512,secret.getBytes())
                .compact();



    }

}

