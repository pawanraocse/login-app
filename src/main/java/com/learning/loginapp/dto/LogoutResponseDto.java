package com.learning.loginapp.dto;

public record LogoutResponseDto(
    boolean success,
    String error
) {}

