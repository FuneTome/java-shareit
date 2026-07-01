package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
}
