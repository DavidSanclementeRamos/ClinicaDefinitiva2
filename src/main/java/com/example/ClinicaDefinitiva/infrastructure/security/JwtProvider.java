package com.example.ClinicaDefinitiva.infrastructure.security;

import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    private final String secretKey = "claveSecretaMuySegura";

    public String generateToken(ReadUserIdentityDto user) {
        return Jwts.builder()
                .setSubject(user.email())
                .claim("roles", List.of("ROLE_USER"))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(2))))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

