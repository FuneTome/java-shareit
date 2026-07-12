package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private static final String ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDtoOut createBooking(@RequestHeader(ID_HEADER) long userId, @Valid @RequestBody BookingDto booking) {
        return bookingService.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approvedBooking(@RequestHeader(ID_HEADER) long userId,
                                         @RequestParam boolean approved,
                                         @PathVariable long bookingId) {
        return bookingService.approvedBooking(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBooking(@PathVariable long bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @GetMapping
    public Collection<BookingDtoOut> getUserBookings(@RequestHeader(ID_HEADER) long userId,
                                      @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoOut> getOwnerBookings(@RequestHeader(ID_HEADER) long userId,
                                      @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getOwnerBookings(userId, state);
    }
}
