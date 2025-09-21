package com.learning.loginapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> publicHello() {
        return ResponseEntity.ok("Hello, world! (unsecured)");
    }

    @GetMapping("/secure-hello")
    public ResponseEntity<String> secureHello(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return ResponseEntity.ok("Hello, " + username + "! (secured)");
    }
}

