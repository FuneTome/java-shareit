package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    @NotBlank
    private LocalDateTime start;
    @NotBlank
    private LocalDateTime end;
    @NotBlank
    private Item item;
    @NotBlank
    private User booker;
    @NotBlank
    private BookingStatus status;
}
