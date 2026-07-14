package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private BookingDto bookingDto;
    private BookingDtoOut bookingDtoOut;
    private ItemDtoOut itemDtoOut;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        itemDtoOut = new ItemDtoOut();
        itemDtoOut.setId(1L);
        itemDtoOut.setName("Test Item");
        itemDtoOut.setDescription("Test Description");
        itemDtoOut.setAvailable(true);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@user.com");

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStartDate(start);
        bookingDto.setEndDate(end);

        bookingDtoOut = new BookingDtoOut();
        bookingDtoOut.setId(1L);
        bookingDtoOut.setStartDate(start);
        bookingDtoOut.setEndDate(end);
        bookingDtoOut.setStatus(BookingStatus.WAITING);
        bookingDtoOut.setItem(itemDtoOut);
        bookingDtoOut.setBooker(userDto);
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), any(BookingDto.class))).thenReturn(bookingDtoOut);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker.id").value(1L))
                .andExpect(jsonPath("$.item.id").value(1L));
    }

    @Test
    void approvedBooking_shouldReturnApprovedBooking() throws Exception {
        BookingDtoOut approvedBooking = new BookingDtoOut();
        approvedBooking.setId(1L);
        approvedBooking.setStartDate(bookingDtoOut.getStartDate());
        approvedBooking.setEndDate(bookingDtoOut.getEndDate());
        approvedBooking.setStatus(BookingStatus.APPROVED);
        approvedBooking.setItem(itemDtoOut);
        approvedBooking.setBooker(userDto);

        when(bookingService.approvedBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(approvedBooking);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBooking_shouldReturnBooking() throws Exception {
        when(bookingService.getBooking(anyLong())).thenReturn(bookingDtoOut);

        mockMvc.perform(get("/bookings/{bookingId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void getUserBookings_shouldReturnUserBookings() throws Exception {
        Collection<BookingDtoOut> bookings = List.of(bookingDtoOut);
        when(bookingService.getUserBookings(anyLong(), any(ru.practicum.shareit.booking.model.BookingState.class)))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getOwnerBookings_shouldReturnOwnerBookings() throws Exception {
        Collection<BookingDtoOut> bookings = List.of(bookingDtoOut);
        when(bookingService.getOwnerBookings(anyLong(), any(ru.practicum.shareit.booking.model.BookingState.class)))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getUserBookings_shouldReturnEmptyCollectionWhenNoBookings() throws Exception {
        when(bookingService.getUserBookings(anyLong(), any(ru.practicum.shareit.booking.model.BookingState.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getOwnerBookings_shouldReturnEmptyCollectionWhenNoBookings() throws Exception {
        when(bookingService.getOwnerBookings(anyLong(), any(ru.practicum.shareit.booking.model.BookingState.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void approvedBooking_shouldReturnRejectedBooking() throws Exception {
        BookingDtoOut rejectedBooking = new BookingDtoOut();
        rejectedBooking.setId(1L);
        rejectedBooking.setStartDate(bookingDtoOut.getStartDate());
        rejectedBooking.setEndDate(bookingDtoOut.getEndDate());
        rejectedBooking.setStatus(BookingStatus.REJECTED);
        rejectedBooking.setItem(itemDtoOut);
        rejectedBooking.setBooker(userDto);

        when(bookingService.approvedBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(rejectedBooking);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }
}