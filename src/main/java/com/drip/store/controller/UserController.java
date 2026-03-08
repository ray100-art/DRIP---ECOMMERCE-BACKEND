package com.drip.store.controller;

import com.drip.store.model.User;
import com.drip.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ── GET /api/users ─────────────────────────────────────────────────
    // Admin: returns all registered users/customers
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}