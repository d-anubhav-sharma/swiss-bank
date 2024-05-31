package com.swiss.bank.user.service.util;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenUtil {

	@Value("${swiss.jwt.util.secret-hashing-key}")
	private String secretHashingKey;
	private static final String ISSUER_NAME = "admin@swiss-bank.com";
	private static final long EXPIRATION_TIME_MILLIS = 3600 * 1000L;

	private Claims getClaimsFromAuthToken(String authToken) {
		return Jwts
				.parser()
				.setSigningKey(secretHashingKey)
				.parseClaimsJws(authToken)
				.getBody();
	}

	public boolean validateToken(String authToken) {
		Claims claims = getClaimsFromAuthToken(authToken);
		return claims
				.getIssuer()
				.equals(ISSUER_NAME) && 
			claims
				.getIssuedAt()
				.after(new Date(System.currentTimeMillis() - EXPIRATION_TIME_MILLIS)) && 
			claims
				.getExpiration()
				.after(new Date());
	}

	public String generateAuthToken(String username) {
		log.atInfo().log("Generating auth token for username: {}", username);
		return Jwts.builder()
				.setSubject(username)
				.setIssuer(ISSUER_NAME)
				.addClaims(Map.of(SwissConstants.USERNAME, username))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
				.signWith(SignatureAlgorithm.HS512, secretHashingKey)
				.compact();
	}
}
