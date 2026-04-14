package yiroma.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yiroma.budgetmanagement.dto.RegisterRequest;
import yiroma.budgetmanagement.dto.UserResponse;
import yiroma.budgetmanagement.enums.SubscriptionPlan;
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
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .emailVerified(false)
                .subscription(freeSubscription)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return UserMapper.toResponse(userRepository.save(user));
    }
}
