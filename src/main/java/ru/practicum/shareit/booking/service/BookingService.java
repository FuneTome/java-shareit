package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingDtoOut createBooking(long userId, BookingDto booking);

    BookingDtoOut approvedBooking(long userId, boolean approved, long bookingId);

    BookingDtoOut getBooking(long bookingId);

    Collection<BookingDtoOut> getUserBookings (long userId, BookingState approved);

    Collection<BookingDtoOut> getOwnerBookings(long userId, BookingState state);
}
