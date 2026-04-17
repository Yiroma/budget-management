package yiroma.budgetmanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import yiroma.budgetmanagement.dto.SubscriptionResponse;
import yiroma.budgetmanagement.enums.SubscriptionPlan;
import yiroma.budgetmanagement.service.SubscriptionService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {"jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3RzLW9ubHktMzItYnl0ZXM=",
		"jwt.expiration=86400000"})
class SubscriptionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SubscriptionService subscriptionService;

	@Test
	void listSubscriptions_returns200WithList() throws Exception {
		List<SubscriptionResponse> subscriptions = List.of(
				new SubscriptionResponse(1, SubscriptionPlan.FREE, 1, 1, 2, true),
				new SubscriptionResponse(2, SubscriptionPlan.PREMIUM, -1, -1, -1, false));

		when(subscriptionService.getAll()).thenReturn(subscriptions);

		mockMvc.perform(get("/api/v1/subscriptions")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].plan").value("FREE"))
				.andExpect(jsonPath("$[1].plan").value("PREMIUM"));
	}

	@Test
	void listSubscriptions_whenEmpty_returns200WithEmptyList() throws Exception {
		when(subscriptionService.getAll()).thenReturn(List.of());

		mockMvc.perform(get("/api/v1/subscriptions")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
}
