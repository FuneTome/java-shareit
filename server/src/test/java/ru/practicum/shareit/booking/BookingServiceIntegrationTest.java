package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    public void getUserBookingTest() {
        User user = userRepository.save(User.builder()
                .name("name")
                .email("e@mail.ru")
                .build());

        User booker = userRepository.save(User.builder()
                .name("booker")
                .email("booker@mail.ru")
                .build());

        Item item = itemRepository.save(Item.builder()
                .name("name")
                .description("description")
                .owner(user)
                .available(true)
                .build());

        Booking first = Booking.builder()
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        Booking second = Booking.builder()
                .startDate(LocalDateTime.now().plusDays(3))
                .endDate(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();


        List<Booking> savedBookings = bookingRepository.saveAll(List.of(first, second));

        List<BookingDtoOut> result = bookingService.getUserBookings(booker.getId(), BookingState.ALL)
                .stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        List<Long> expectedIds = savedBookings.stream().map(Booking::getId).sorted().toList();
        List<Long> actualIds = result.stream().map(BookingDtoOut::getId).sorted().toList();
        Assertions.assertEquals(expectedIds, actualIds);
    }
}
