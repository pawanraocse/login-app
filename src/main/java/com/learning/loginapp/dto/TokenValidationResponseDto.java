package com.learning.loginapp.dto;

import java.util.Map;

public record TokenValidationResponseDto(
    boolean valid,
    Map<String, Object> claims,
    String error
) {}

