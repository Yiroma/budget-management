package yiroma.budgetmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yiroma.budgetmanagement.dto.SubscriptionUpdateRequest;
import yiroma.budgetmanagement.dto.UserResponse;
import yiroma.budgetmanagement.dto.UserUpdateRequest;
import yiroma.budgetmanagement.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public UserResponse getMe(Principal principal) {
		return userService.getMe(principal.getName());
	}

	@PutMapping("/me")
	public UserResponse updateMe(Principal principal, @Valid @RequestBody UserUpdateRequest request) {
		return userService.updateMe(principal.getName(), request);
	}

	@DeleteMapping("/me")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMe(Principal principal) {
		userService.deleteMe(principal.getName());
	}

	@PutMapping("/me/subscription")
	public UserResponse updateSubscription(Principal principal, @Valid @RequestBody SubscriptionUpdateRequest request) {
		return userService.updateSubscription(principal.getName(), request);
	}
}
