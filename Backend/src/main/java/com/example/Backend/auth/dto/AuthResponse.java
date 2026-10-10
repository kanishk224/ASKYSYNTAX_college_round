package com.example.Backend.auth.dto;

public record AuthResponse(
		Long userId,
		String fullName,
		String email,
		String accessToken,
		String tokenType,
		long expiresIn) {
}
