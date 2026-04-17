package yiroma.budgetmanagement.mapper;

import yiroma.budgetmanagement.dto.UserResponse;
import yiroma.budgetmanagement.model.User;

public class UserMapper {

	private UserMapper() {
	}

	public static UserResponse toResponse(User user) {
		return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getEmailVerified(),
				SubscriptionMapper.toResponse(user.getSubscription()), user.getCreatedAt(), user.getUpdatedAt());
	}
}
