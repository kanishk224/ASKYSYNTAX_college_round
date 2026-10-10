package com.example.Backend.auth;

import java.util.Locale;

import com.example.Backend.auth.dto.AuthResponse;
import com.example.Backend.auth.dto.LoginRequest;
import com.example.Backend.auth.dto.RegisterRequest;
import com.example.Backend.common.BusinessException;
import com.example.Backend.auth.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		String email = normalizeEmail(request.email());
		if (userRepository.existsByEmail(email)) {
			throw new BusinessException("An account with this email already exists", HttpStatus.CONFLICT);
		}

		User user = userRepository.save(new User(
				request.fullName().trim(),
				email,
				passwordEncoder.encode(request.password())));
		return createAuthResponse(user);
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(normalizeEmail(request.email()))
				.filter(candidate -> passwordEncoder.matches(request.password(), candidate.getPassword()))
				.orElseThrow(() -> new BusinessException("Invalid email or password", HttpStatus.UNAUTHORIZED));
		return createAuthResponse(user);
	}

	private AuthResponse createAuthResponse(User user) {
		return new AuthResponse(
				user.getId(),
				user.getFullName(),
				user.getEmail(),
				jwtService.generateToken(user.getEmail()),
				"Bearer",
				jwtService.getExpirationMs() / 1000);
	}

	private String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
