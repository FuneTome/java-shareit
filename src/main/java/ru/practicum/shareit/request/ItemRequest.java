package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Long id;
    @NotBlank
    private String description;
    @NotBlank
    private User requestor;
    @NotBlank
    private LocalDateTime created;
}
