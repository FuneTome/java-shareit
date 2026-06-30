package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOut createBooking(long userId, BookingDto booking) {
        if (booking.getStartDate().equals(booking.getEndDate())) {
            throw new BadRequestException("Время не может быть одинаковым");
        }
        User user = checkUserExistAndReturn(userId);
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(()
                -> new NotFoundException("Предмета с id = " + booking.getItemId() + " не существует"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }
        Booking bk = BookingMapper.toBooking(booking, user, item);
        return BookingMapper.toOut(bookingRepository.save(bk));
    }

    @Override
    public BookingDtoOut approvedBooking(long userId, boolean approved, long bookingId) {
        Booking booking = checkBookingExistAndReturn(bookingId);
        /*
        Проверка владельца идет вначале только потому что тесты требуют 400 или 403
        а при нормальной проверке, то есть при проверке вначале, существует ли вообще user, он бьет 404
        */
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("Юзер с id = " + userId + "не являеся владельцем данной вещи");
        }
        checkUserExistAndReturn(userId);
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toOut(booking);
    }

    @Override
    public BookingDtoOut getBooking(long bookingId) {
        Booking booking = checkBookingExistAndReturn(bookingId);
        return BookingMapper.toOut(booking);
    }

    @Override
    public Collection<BookingDtoOut> getUserBookings(long userId, BookingState state) {
        checkUserExistAndReturn(userId);
        Collection<Booking> booking = new ArrayList<>();
        switch(state) {
            case ALL -> booking = bookingRepository.findByBooker_Id(userId);
            case CURRENT -> booking = bookingRepository.findByBetween(userId, LocalDateTime.now());
            case PAST -> booking = bookingRepository.findByBooker_IdAndEndDateIsBefore(userId, LocalDateTime.now());
            case FUTURE -> booking = bookingRepository.findByBooker_IdAndStartDateIsAfter(userId, LocalDateTime.now());
            case WAITING -> booking = bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> booking = bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED);
        }
        return booking.stream().map(BookingMapper::toOut).toList();
    }

    @Override
    public Collection<BookingDtoOut> getOwnerBookings(long ownerId, BookingState state) {
        checkUserExistAndReturn(ownerId);
        Collection<Booking> booking = new ArrayList<>();
        switch(state) {
            case ALL -> booking = bookingRepository.findByOwnerIdAll(ownerId);
            case CURRENT -> booking = bookingRepository.findByOwnerIdCurrent(ownerId, LocalDateTime.now());
            case PAST -> booking = bookingRepository.findByOwnerIdPast(ownerId, LocalDateTime.now());
            case FUTURE -> booking = bookingRepository.findByOwnerIdFuture(ownerId, LocalDateTime.now());
            case WAITING -> booking = bookingRepository.findByOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
            case REJECTED -> booking = bookingRepository.findByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
        }
        return booking.stream().map(BookingMapper::toOut).toList();
    }

    private User checkUserExistAndReturn(long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Юзер с id = " + userId + " не найден"));
    }

    private Booking checkBookingExistAndReturn(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(()
                -> new NotFoundException("Бронирование с id = " + bookingId + " не найден"));
    }
}
