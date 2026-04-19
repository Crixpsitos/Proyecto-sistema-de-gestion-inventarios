package gestion_inventarios.backend.infrastructure.in.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long expirationSeconds;

    @Value("${security.jwt.refresh.expire-length}")
    private long refreshExpirationSeconds;

    // --- Generación ---

    public String generateToken(UserDetails userDetails) {
        return buildToken(Map.of(CLAIM_TYPE, TYPE_ACCESS), userDetails, expirationSeconds);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(Map.of(CLAIM_TYPE, TYPE_REFRESH), userDetails, refreshExpirationSeconds);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long ttlSeconds) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ttlSeconds * 1000);

        return Jwts.builder()
            .header().add("typ", "JWT").and()
            .claims(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(getSigningKey())
            .compact();
    }

    // --- Extracción ---

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject().equals(userDetails.getUsername())
            && !claims.getExpiration().before(new Date())
            && TYPE_ACCESS.equals(claims.get(CLAIM_TYPE));
    }

    
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject().equals(userDetails.getUsername())
            && !claims.getExpiration().before(new Date())
            && TYPE_REFRESH.equals(claims.get(CLAIM_TYPE));
    }

    // --- Privados ---

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
