package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {
    public static Booking toBooking(BookingDto dto, User user, Item item) {
        Booking booking = new Booking();
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }

    public static BookingDtoOut toOut(Booking booking) {
        BookingDtoOut out = new BookingDtoOut();
        out.setId(booking.getId());
        out.setStartDate(booking.getStartDate());
        out.setEndDate(booking.getEndDate());
        out.setItem(ItemMapper.toOut(booking.getItem()));
        out.setBooker(UserMapper.toDto(booking.getBooker()));
        out.setStatus(booking.getStatus());
        return out;
    }
}
