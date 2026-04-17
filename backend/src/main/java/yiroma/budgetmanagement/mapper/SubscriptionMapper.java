package yiroma.budgetmanagement.mapper;

import yiroma.budgetmanagement.dto.SubscriptionResponse;
import yiroma.budgetmanagement.model.Subscription;

public class SubscriptionMapper {

    private SubscriptionMapper() {
    }

    public static SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getPlan(),
                subscription.getMaxAccounts(),
                subscription.getMaxBudgets(),
                subscription.getMaxMembersPerBudget(),
                subscription.getHasAds()
        );
    }
}
