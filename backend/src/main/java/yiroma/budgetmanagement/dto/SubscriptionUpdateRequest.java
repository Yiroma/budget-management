package yiroma.budgetmanagement.dto;

import jakarta.validation.constraints.NotNull;

public record SubscriptionUpdateRequest(
        @NotNull(message = "L'identifiant de l'abonnement est obligatoire.")
        Integer subscriptionId
) {
}
