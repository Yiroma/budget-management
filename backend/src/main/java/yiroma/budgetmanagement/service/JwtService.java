package yiroma.budgetmanagement.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiroma.budgetmanagement.config.JwtProperties;
import yiroma.budgetmanagement.model.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtProperties jwtProperties;

	public String generateToken(User user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.expiration());

		return Jwts.builder().claim("userId", user.getId().toString()).issuedAt(now).expiration(expiry)
				.signWith(getSigningKey()).compact();
	}

	public Optional<UUID> extractUserIdSafely(String token) {
		try {
			String userId = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload()
					.get("userId", String.class);
			return Optional.of(UUID.fromString(userId));
		} catch (JwtException | IllegalArgumentException e) {
			return Optional.empty();
		}
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
	}
}
