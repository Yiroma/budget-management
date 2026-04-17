package yiroma.budgetmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import yiroma.budgetmanagement.enums.SubscriptionPlan;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "plan", nullable = false, unique = true, length = 20)
	private SubscriptionPlan plan;

	@Column(name = "max_accounts", nullable = false)
	private Integer maxAccounts;

	@Column(name = "max_budgets", nullable = false)
	private Integer maxBudgets;

	@Column(name = "max_members_per_budget", nullable = false)
	private Integer maxMembersPerBudget;

	@Column(name = "has_ads", nullable = false)
	private Boolean hasAds;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
}
