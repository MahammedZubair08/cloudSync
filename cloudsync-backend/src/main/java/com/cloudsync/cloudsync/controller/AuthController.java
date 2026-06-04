package com.cloudsync.cloudsync.controller;

import com.cloudsync.cloudsync.dto.RegisterRequest;
import com.cloudsync.cloudsync.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.cloudsync.cloudsync.dto.LoginRequest;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        return authService.login(request);
    }
}
