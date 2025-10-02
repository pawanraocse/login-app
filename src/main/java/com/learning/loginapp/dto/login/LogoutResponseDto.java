package com.learning.loginapp.dto.login;

public record LogoutResponseDto(
    boolean success,
    String error
) {}
