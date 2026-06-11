package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.item.availabilityStatus;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private availabilityStatus availably;
    @NotBlank
    private User owner;
    private ItemRequest request;
}
