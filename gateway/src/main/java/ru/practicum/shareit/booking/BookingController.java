package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;
    private static final String ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestHeader(ID_HEADER) long userId, @Valid @RequestBody BookingDto booking) {
        return bookingClient.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvedBooking(@RequestHeader(ID_HEADER) long userId,
                                         @RequestParam boolean approved,
                                         @PathVariable long bookingId) {
        return bookingClient.approvedBooking(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable long bookingId) {
        return bookingClient.getBooking(bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(ID_HEADER) long userId,
                                                     @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingClient.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(ID_HEADER) long userId,
                                                      @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingClient.getOwnerBookings(userId, state);
    }
}
