package org.messaging.auth.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.messaging.auth.auth.model.JwtAuthenticationResponse;
import org.messaging.auth.auth.model.LoginRequest;
import org.messaging.auth.auth.model.RegisterRequest;
import org.messaging.auth.exception.model.UserNotLoggedInException;
import org.messaging.auth.user.entity.User;
import org.messaging.auth.user.model.Role;
import org.messaging.auth.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${auth.jwt.expiration}")
    private Long jwtExpirationMs;
    @Value("${cookie.domain}")
    private String domain;
    @Value("${cookie.name}")
    private String cookieName;
    @Value("${cookie.secure}")
    private boolean isSecure;

    public JwtAuthenticationResponse register(RegisterRequest registerRequest, HttpServletResponse response) {
        User user = User
                .builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.ROLE_USER)
                .build();

        user = userService.save(user);
        String jwt = jwtService.generateToken(user);
        response.addCookie(createJwtCookie(jwt, (int) (jwtExpirationMs / 1000)));
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    public JwtAuthenticationResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        String jwt = jwtService.generateToken(user);
        response.addCookie(createJwtCookie(jwt, (int) (jwtExpirationMs / 1000)));
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public boolean userHasRole(String... roles) {
        User user;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UserNotLoggedInException();
        }

        return Arrays.asList(roles).contains(user.getRole().toString().replaceFirst("ROLE_", ""));
    }

    public void logout(HttpServletResponse response) {
        response.addCookie(createJwtCookie(null, 0));
    }

    private Cookie createJwtCookie(String token, int maxAge) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setDomain(domain);
        cookie.setSecure(isSecure);
        cookie.setPath("/");
        return cookie;
    }
}