package com.example.jwtspring.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private final String SECRETE_KEY = "655468576D5A7134743777397A24432646294A404E635266556A586E32723575";

    public String extractUsername(String token) {
        return  extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
         return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolve){
        Claims claims =extractAllClaim(token);
        return claimsResolve.apply(claims);
    }

    public Claims  extractAllClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] signKey = Decoders.BASE64.decode(SECRETE_KEY);
       return Keys.hmacShaKeyFor(signKey);
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public  String generateToken(Map<String,Object> extractClaims,
                                 UserDetails userDetails
){   return Jwts.builder()
                 .setSubject(userDetails.getUsername())
                 .setClaims(extractClaims)
                 .setExpiration(new Date(System.currentTimeMillis()  + 1000*60*60*24))
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                 .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
         final String username = extractUsername(token);
                 return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
