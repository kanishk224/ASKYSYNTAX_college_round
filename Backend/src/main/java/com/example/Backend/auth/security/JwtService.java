package com.example.Backend.auth.security;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	private final SecretKey signingKey;
	private final long expirationMs;

	public JwtService(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration-ms}") long expirationMs) {
		this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.expirationMs = expirationMs;
	}

	public String generateToken(String subject) {
		Instant issuedAt = Instant.now();
		return Jwts.builder()
				.subject(subject)
				.issuedAt(Date.from(issuedAt))
				.expiration(Date.from(issuedAt.plusMillis(expirationMs)))
				.signWith(signingKey)
				.compact();
	}

	public String extractSubject(String token) {
		return parseToken(token).getPayload().getSubject();
	}

	public boolean isTokenValid(String token) {
		try {
			parseToken(token);
			return true;
		} catch (JwtException | IllegalArgumentException exception) {
			return false;
		}
	}

	public long getExpirationMs() {
		return expirationMs;
	}

	private io.jsonwebtoken.Jws<Claims> parseToken(String token) {
		return Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token);
	}
}