package yiroma.budgetmanagement.dto;

import yiroma.budgetmanagement.enums.SubscriptionPlan;

public record SubscriptionResponse(Integer id, SubscriptionPlan plan, Integer maxAccounts, Integer maxBudgets,
		Integer maxMembersPerBudget, Boolean hasAds) {
}
