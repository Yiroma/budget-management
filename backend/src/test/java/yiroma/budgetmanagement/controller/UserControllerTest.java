package yiroma.budgetmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import yiroma.budgetmanagement.dto.SubscriptionResponse;
import yiroma.budgetmanagement.dto.SubscriptionUpdateRequest;
import yiroma.budgetmanagement.dto.UserResponse;
import yiroma.budgetmanagement.dto.UserUpdateRequest;
import yiroma.budgetmanagement.enums.SubscriptionPlan;
import yiroma.budgetmanagement.exception.NotFoundException;
import yiroma.budgetmanagement.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {"jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3RzLW9ubHktMzItYnl0ZXM=",
		"jwt.expiration=86400000"})
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	private ObjectMapper objectMapper;
	private UserResponse userResponse;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		SubscriptionResponse subscription = new SubscriptionResponse(1, SubscriptionPlan.FREE, 1, 1, 2, true);
		userResponse = new UserResponse(UUID.randomUUID(), "user@example.com", "Jean Dupont", false, subscription,
				LocalDateTime.now(), LocalDateTime.now());
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void getMe_withAuthenticatedUser_returns200() throws Exception {
		when(userService.getMe("user@example.com")).thenReturn(userResponse);

		mockMvc.perform(get("/api/v1/users/me")).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("user@example.com"))
				.andExpect(jsonPath("$.name").value("Jean Dupont"))
				.andExpect(jsonPath("$.subscription.plan").value("FREE"));
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void updateMe_withValidBody_returns200() throws Exception {
		UserUpdateRequest request = new UserUpdateRequest("Nouveau Nom", null, null);
		when(userService.updateMe(eq("user@example.com"), any(UserUpdateRequest.class))).thenReturn(userResponse);

		mockMvc.perform(put("/api/v1/users/me").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("user@example.com"));
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void updateMe_withInvalidEmail_returns400() throws Exception {
		String invalidBody = """
				{ "email": "not-an-email" }
				""";

		mockMvc.perform(put("/api/v1/users/me").contentType(MediaType.APPLICATION_JSON).content(invalidBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void deleteMe_withAuthenticatedUser_returns204() throws Exception {
		doNothing().when(userService).deleteMe("user@example.com");

		mockMvc.perform(delete("/api/v1/users/me")).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void updateSubscription_withValidId_returns200() throws Exception {
		SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(2);

		SubscriptionResponse premiumSubscription = new SubscriptionResponse(2, SubscriptionPlan.PREMIUM, -1, -1, -1,
				false);
		UserResponse premiumUserResponse = new UserResponse(userResponse.id(), userResponse.email(),
				userResponse.name(), userResponse.emailVerified(), premiumSubscription, userResponse.createdAt(),
				userResponse.updatedAt());

		when(userService.updateSubscription(eq("user@example.com"), any(SubscriptionUpdateRequest.class)))
				.thenReturn(premiumUserResponse);

		mockMvc.perform(put("/api/v1/users/me/subscription").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.subscription.plan").value("PREMIUM"));
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void updateSubscription_withUnknownId_returns404() throws Exception {
		SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(99);

		when(userService.updateSubscription(eq("user@example.com"), any(SubscriptionUpdateRequest.class)))
				.thenThrow(new NotFoundException("Abonnement introuvable."));

		mockMvc.perform(put("/api/v1/users/me/subscription").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail").value("Abonnement introuvable."));
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void updateSubscription_withMissingId_returns400() throws Exception {
		String invalidBody = "{}";

		mockMvc.perform(
				put("/api/v1/users/me/subscription").contentType(MediaType.APPLICATION_JSON).content(invalidBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "unknown@example.com")
	void getMe_withUnknownUser_returns404() throws Exception {
		doThrow(new NotFoundException("Utilisateur introuvable.")).when(userService).getMe("unknown@example.com");

		mockMvc.perform(get("/api/v1/users/me")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail").value("Utilisateur introuvable."));
	}
}
