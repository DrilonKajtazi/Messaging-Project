package org.messaging.auth.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.messaging.auth.auth.model.JwtAuthenticationResponse;
import org.messaging.auth.auth.model.LoginRequest;
import org.messaging.auth.auth.model.RegisterRequest;
import org.messaging.auth.auth.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtAuthenticationResponse register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        JwtAuthenticationResponse jwtToken = authenticationService.register(request,response);
        return jwtToken;
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        JwtAuthenticationResponse jwtToken = authenticationService.login(request,response);
        return jwtToken;
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response){
        authenticationService.logout(response);
        return ResponseEntity.noContent().build();
    }

}
