package com.learning.loginapp.exception;

/**
 * Domain-specific exception for tenant onboarding failures.
 * Wraps low-level errors and provides clear context for API and logging.
 */
public class TenantOnboardingException extends RuntimeException {
    public TenantOnboardingException(String message) {
        super(message);
    }
    public TenantOnboardingException(String message, Throwable cause) {
        super(message, cause);
    }
}

