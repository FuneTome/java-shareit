package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService requestService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private ItemRequestDto itemRequestDto;
    private ItemRequestDtoOut itemRequestDtoOut;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@user.com");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Need a drill");

        itemRequestDtoOut = new ItemRequestDtoOut();
        itemRequestDtoOut.setId(1L);
        itemRequestDtoOut.setDescription("Need a drill");
        itemRequestDtoOut.setCreated(now);
        itemRequestDtoOut.setRequestor(userDto); // установка объекта UserDto, а не long
    }

    @Test
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        when(requestService.createRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(itemRequestDtoOut);

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a drill"))
                .andExpect(jsonPath("$.requestor.id").value(1L)); // проверяем вложенный объект
    }

    @Test
    void getUserRequests_shouldReturnCollectionOfRequests() throws Exception {
        Collection<ItemRequestDtoOut> requests = List.of(itemRequestDtoOut);
        when(requestService.getUserRequests(anyLong())).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Need a drill"))
                .andExpect(jsonPath("$[0].requestor.id").value(1L));
    }

    @Test
    void getAllRequests_shouldReturnAllRequests() throws Exception {
        Collection<ItemRequestDtoOut> requests = List.of(itemRequestDtoOut);
        when(requestService.getAllRequests()).thenReturn(requests);

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Need a drill"))
                .andExpect(jsonPath("$[0].requestor.id").value(1L));
    }

    @Test
    void getRequest_shouldReturnRequest() throws Exception {
        when(requestService.getRequest(anyLong())).thenReturn(itemRequestDtoOut);

        mockMvc.perform(get("/requests/{requestId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a drill"))
                .andExpect(jsonPath("$.requestor.id").value(1L));
    }

    @Test
    void getUserRequests_shouldReturnEmptyCollectionWhenNoRequests() throws Exception {
        when(requestService.getUserRequests(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllRequests_shouldReturnEmptyCollectionWhenNoRequests() throws Exception {
        when(requestService.getAllRequests()).thenReturn(List.of());

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}