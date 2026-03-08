package com.drip.store.controller;

import com.drip.store.config.JwtUtil;
import com.drip.store.model.User;
import com.drip.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // ── POST /api/auth/register ────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email     = body.get("email");
        String password  = body.get("password");
        String firstName = body.get("firstName");
        String lastName  = body.get("lastName");

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already registered"));
        }

        // Create and save new user with hashed password
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole("ROLE_USER");

        userRepository.save(user);

        // Generate token so user is logged in immediately after registering
        String token = jwtUtil.generateToken(email);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "token", token,
                "email", email,
                "firstName", firstName
        ));
    }

    // ── POST /api/auth/login ───────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        try {
            // This checks the email + password against the database
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        // Password was correct — generate and return token
        String token = jwtUtil.generateToken(email);

        User user = userRepository.findByEmail(email).get();

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", email,
                "firstName", user.getFirstName()
        ));
    }
}