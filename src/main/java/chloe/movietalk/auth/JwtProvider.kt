package chloe.movietalk.auth;

import chloe.movietalk.dto.response.user.UserInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtProvider {

    private final Key secretKey;

    private final Long accessTokenExpTime;

    private final Long refreshTokenExpTime;

    public JwtProvider(
            @Value("${jwt.secret}") final String secretKey,
            @Value("${jwt.access_expiration_time}") final Long accessTokenExpTime,
            @Value("${jwt.refresh_expiration_time}") final Long refreshTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public String generateAccessToken(UserInfo user) {
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setHeader(createHeader())
                .setSubject(userId.toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public UUID getUserId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = parseClaims(token).getExpiration();
            return expirationDate != null && expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT", e);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty", e);
        }

        return false;
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private Map<String, Object> createClaims(UserInfo user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("userRole", user.getRole());
        return claims;
    }
}
