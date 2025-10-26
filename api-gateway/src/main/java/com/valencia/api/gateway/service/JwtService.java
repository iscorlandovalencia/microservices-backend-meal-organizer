package com.valencia.api.gateway.service;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
    private final String SECRET_KEY = "clave-secreta-supersegura";

    public boolean isTokenValid(String token){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJwt(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }
}
