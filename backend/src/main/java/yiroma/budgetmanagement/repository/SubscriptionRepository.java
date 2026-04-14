package yiroma.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiroma.budgetmanagement.enums.SubscriptionPlan;
import yiroma.budgetmanagement.model.Subscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Optional<Subscription> findByPlan(SubscriptionPlan plan);
}
