package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.dto.auth.AuthResponse;
import ru.effectivemobile.dto.auth.AuthRequest;
import ru.effectivemobile.service.AuthService;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final Logger log = Logger.getLogger(AuthController.class);

    @PostMapping("/login")
    public AuthResponse createAuthToken(
            @RequestBody AuthRequest authRequest) {
        log.info("POST Request: authorization attempt for LOGIN: " + authRequest.login());
        return authService.createAuthToken(authRequest);
    }
}
