package yiroma.budgetmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import yiroma.budgetmanagement.dto.LoginRequest;
import yiroma.budgetmanagement.dto.TokenResponse;
import yiroma.budgetmanagement.exception.UnauthorizedException;
import yiroma.budgetmanagement.repository.UserRepository;
import yiroma.budgetmanagement.service.AuthService;
import yiroma.budgetmanagement.service.JwtService;
import yiroma.budgetmanagement.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {"jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3RzLW9ubHktMzItYnl0ZXM=",
		"jwt.expiration=86400000"})
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private AuthService authService;

	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private UserRepository userRepository;

	@Test
	void login_withValidCredentials_returns200WithToken() throws Exception {
		LoginRequest request = new LoginRequest("user@example.com", "SecureP@ss123");
		TokenResponse response = new TokenResponse("eyJhbGciOiJIUzI1NiIs.fake.token", "Bearer");

		when(authService.login(any(LoginRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").value(response.accessToken()))
				.andExpect(jsonPath("$.tokenType").value("Bearer"));
	}

	@Test
	void login_withInvalidPassword_returns401() throws Exception {
		LoginRequest request = new LoginRequest("user@example.com", "WrongPassword1!");

		when(authService.login(any(LoginRequest.class)))
				.thenThrow(new UnauthorizedException("Identifiants incorrects."));

		mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.detail").value("Identifiants incorrects."));
	}

	@Test
	void login_withUnknownEmail_returns401() throws Exception {
		LoginRequest request = new LoginRequest("unknown@example.com", "SecureP@ss123");

		when(authService.login(any(LoginRequest.class)))
				.thenThrow(new UnauthorizedException("Identifiants incorrects."));

		mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.detail").value("Identifiants incorrects."));
	}

	@Test
	void login_withInvalidBody_returns400() throws Exception {
		String invalidBody = """
				{ "email": "not-an-email", "password": "" }
				""";

		mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(invalidBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	void logout_returns204() throws Exception {
		mockMvc.perform(post("/api/v1/auth/logout")).andExpect(status().isNoContent());
	}
}
