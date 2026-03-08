package com.drip.store.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;  // stored as bcrypt hash, never plain text

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String role;  // "ROLE_USER" or "ROLE_ADMIN"
}