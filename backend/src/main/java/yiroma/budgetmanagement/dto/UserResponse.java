package yiroma.budgetmanagement.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String name,
        Boolean emailVerified,
        SubscriptionResponse subscription,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
