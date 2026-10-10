package com.example.Backend.auth;

import com.example.Backend.auth.dto.AuthResponse;
import com.example.Backend.auth.dto.LoginRequest;
import com.example.Backend.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public String register(@Valid @RequestBody RegisterRequest request) {
		AuthResponse authResponse = authService.register(request);
		return authResponse.accessToken();
	}

	@PostMapping("/login")
	public String login(@Valid @RequestBody LoginRequest request) {
		AuthResponse authResponse = authService.login(request);
		return authResponse.accessToken();
	}
}
