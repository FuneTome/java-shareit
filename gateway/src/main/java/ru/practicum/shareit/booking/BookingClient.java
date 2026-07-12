package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(RestTemplateBuilder builder, @Value("http://localhost:9090") String serverUrl) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, Object booking) {
        return post(API_PREFIX, userId, booking);
    }

    public ResponseEntity<Object> approvedBooking(long userId, boolean approved, long bookingId) {
        return patch(API_PREFIX + "/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBooking(long bookingId) {
        return get(API_PREFIX + "/" + bookingId);
    }

    public ResponseEntity<Object> getUserBookings(long userId, BookingState state) {
        return get(API_PREFIX + "?state=" + state, userId);
    }

    public ResponseEntity<Object> getOwnerBookings(long userId, BookingState state) {
        return get(API_PREFIX + "/owner?state=" + state, userId);
    }
}
