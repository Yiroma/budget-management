package yiroma.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yiroma.budgetmanagement.dto.LoginRequest;
import yiroma.budgetmanagement.dto.TokenResponse;
import yiroma.budgetmanagement.exception.UnauthorizedException;
import yiroma.budgetmanagement.model.User;
import yiroma.budgetmanagement.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public TokenResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new UnauthorizedException("Identifiants incorrects."));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new UnauthorizedException("Identifiants incorrects.");
		}

		String token = jwtService.generateToken(user);
		return new TokenResponse(token, "Bearer");
	}
}
