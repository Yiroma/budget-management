package yiroma.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yiroma.budgetmanagement.dto.RegisterRequest;
import yiroma.budgetmanagement.dto.SubscriptionUpdateRequest;
import yiroma.budgetmanagement.dto.UserResponse;
import yiroma.budgetmanagement.dto.UserUpdateRequest;
import yiroma.budgetmanagement.enums.SubscriptionPlan;
import yiroma.budgetmanagement.exception.NotFoundException;
import yiroma.budgetmanagement.exception.UnprocessableException;
import yiroma.budgetmanagement.mapper.UserMapper;
import yiroma.budgetmanagement.model.Subscription;
import yiroma.budgetmanagement.model.User;
import yiroma.budgetmanagement.repository.SubscriptionRepository;
import yiroma.budgetmanagement.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new UnprocessableException("Impossible de créer le compte avec ces informations.");
		}

		Subscription freeSubscription = subscriptionRepository.findByPlan(SubscriptionPlan.FREE)
				.orElseThrow(() -> new IllegalStateException("Plan FREE introuvable en base."));

		LocalDateTime now = LocalDateTime.now();
		User user = User.builder().email(request.email()).password(passwordEncoder.encode(request.password()))
				.name(request.name()).emailVerified(false).subscription(freeSubscription).createdAt(now).updatedAt(now)
				.build();

		return UserMapper.toResponse(userRepository.save(user));
	}

	public UserResponse getMe(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));
		return UserMapper.toResponse(user);
	}

	@Transactional
	public UserResponse updateMe(String email, UserUpdateRequest request) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

		if (request.name() != null) {
			user.setName(request.name());
		}
		if (request.email() != null) {
			// TODO : vérifier que l'utilisateur possède le nouvel email (envoi d'un lien de
			// confirmation)
			if (userRepository.existsByEmail(request.email())) {
				throw new UnprocessableException("Cet email est déjà utilisé.");
			}
			user.setEmail(request.email());
		}
		if (request.password() != null) {
			user.setPassword(passwordEncoder.encode(request.password()));
		}

		user.setUpdatedAt(LocalDateTime.now());
		return UserMapper.toResponse(userRepository.save(user));
	}

	@Transactional
	public void deleteMe(String email) {
		// TODO : exiger une confirmation (re-saisie du mot de passe ou confirmation par
		// email) avant suppression
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));
		userRepository.delete(user);
	}

	@Transactional
	public UserResponse updateSubscription(String email, SubscriptionUpdateRequest request) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

		Subscription subscription = subscriptionRepository.findById(request.subscriptionId())
				.orElseThrow(() -> new NotFoundException("Abonnement introuvable."));

		user.setSubscription(subscription);
		user.setUpdatedAt(LocalDateTime.now());
		return UserMapper.toResponse(userRepository.save(user));
	}
}
