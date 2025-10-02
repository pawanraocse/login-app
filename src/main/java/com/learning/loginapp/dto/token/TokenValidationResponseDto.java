package com.learning.loginapp.dto.token;

import java.util.Map;

public record TokenValidationResponseDto(
    boolean valid,
    Map<String, Object> claims,
    String error
) {}
