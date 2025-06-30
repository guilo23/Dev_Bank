package com.bia.dev_bank.security;

import com.bia.dev_bank.entity.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String SECRET_KEY = "01234567890123456789012345678901";
  private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

  private Key getSignKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  public String generateToken(UserDetails userDetails) {
    CustomUserDetails user = (CustomUserDetails) userDetails;

    Map<String, Object> claims = new HashMap<>();
    claims.put("customerId", user.getId());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Long extractCustomerId(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("customerId", Long.class);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  private boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }
}
