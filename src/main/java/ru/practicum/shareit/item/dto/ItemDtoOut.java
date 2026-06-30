package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoOutMini;

import java.util.Collection;

@Data
public class ItemDtoOut {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoOut lastBooking;
    private Collection<CommentDtoOut> comments;
    private BookingDtoOut nextBooking;
}
