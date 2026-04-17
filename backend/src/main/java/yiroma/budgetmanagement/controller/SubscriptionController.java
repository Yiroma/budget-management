package yiroma.budgetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yiroma.budgetmanagement.dto.SubscriptionResponse;
import yiroma.budgetmanagement.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	@GetMapping
	public List<SubscriptionResponse> listSubscriptions() {
		return subscriptionService.getAll();
	}
}
