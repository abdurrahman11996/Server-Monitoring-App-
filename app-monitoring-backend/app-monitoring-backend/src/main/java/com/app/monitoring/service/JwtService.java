package com.app.monitoring.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
   // private final String SECRET = "mysecretkey123456789";
   SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // ⏰ Token valid for 1 hour (in milliseconds)
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1 day
                .signWith(key)
                .compact();
    }

    //read all claims (data) from the token
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    //  Extract username (subject) from token
    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    //extract email from token
//    public String extractEmaill(String token){
//        return getAllClaims(token).getSubject();
//    }

    // 🧩 4. Check if token is expired
    private boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }

    // 🧩 5. Validate the token: check username and expiration
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractEmail(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
