package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private ItemDtoOut itemDtoOut;
    private ItemDto itemDto;
    private CommentDtoOut commentDtoOut;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        itemDtoOut = new ItemDtoOut();
        itemDtoOut.setId(1L);
        itemDtoOut.setName("Test Item");
        itemDtoOut.setDescription("Test Description");
        itemDtoOut.setAvailable(true);

        commentDtoOut = new CommentDtoOut();
        commentDtoOut.setId(1L);
        commentDtoOut.setText("Great item!");
        commentDtoOut.setAuthorName("User");
        commentDtoOut.setCreated(LocalDateTime.now());
    }

    @Test
    void getItems_shouldReturnCollectionOfItems() throws Exception {
        Collection<ItemDtoOut> items = List.of(itemDtoOut);
        when(itemService.getAllItems(anyLong())).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }

    @Test
    void getItem_shouldReturnItem() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDtoOut);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    void searchItems_shouldReturnFoundItems() throws Exception {
        Collection<ItemDtoOut> items = List.of(itemDtoOut);
        when(itemService.searchItems(anyString())).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void addItem_shouldReturnCreatedItem() throws Exception {
        when(itemService.addItem(anyLong(), any(ItemDto.class))).thenReturn(itemDtoOut);

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDtoOut);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    void createComment_shouldReturnCreatedComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        when(itemService.createComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDtoOut);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Great item!"));
    }

    @Test
    void getItems_shouldReturnEmptyCollectionWhenNoItems() throws Exception {
        when(itemService.getAllItems(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchItems_shouldReturnEmptyCollectionWhenTextIsBlank() throws Exception {
        when(itemService.searchItems(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
