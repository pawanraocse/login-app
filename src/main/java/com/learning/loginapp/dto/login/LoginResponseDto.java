package com.learning.loginapp.dto.login;

import java.util.Map;

public record LoginResponseDto(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    Map<String, Object> claims
) {}
