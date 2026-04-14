package yiroma.budgetmanagement.dto;

import yiroma.budgetmanagement.enums.SubscriptionPlan;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String name,
        Boolean emailVerified,
        SubscriptionPlan subscriptionPlan,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
