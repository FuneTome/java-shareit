package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(name = "requestor_id", nullable = false)
    private Long requestorId;
}
