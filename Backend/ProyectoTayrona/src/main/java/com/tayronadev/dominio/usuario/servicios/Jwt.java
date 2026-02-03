package com.tayronadev.dominio.usuario.servicios;

import com.tayronadev.dominio.usuario.modelo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class Jwt {

    @Value("${application.security.jwt.secret-key}")
    private String llaveSecreta;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiracion;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;

    public String generarToken(final User user) {
        return construirToken(user, jwtExpiracion);
    }

    public String generarReinicioToken(final User user) {
        return construirToken(user, jwtRefreshExpiration);
    }

    private String construirToken(final User user, final long expiracion) {
        return Jwts.builder()
                .id(user.getId())
                .subject(user.getCorreo())
                .claim("name", user.getNombre())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(getSignInKey())
                .compact();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String extraerCorreo(String token) {
        return extraerClaims(token).getSubject();
    }

    public boolean esTokenValido(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(llaveSecreta);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
