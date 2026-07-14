package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTest {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;

    @Test
    public void updateItemTest() {
        User user = userRepository.save(User.builder()
                .name("name")
                .email("e@mail.ru")
                .build());

        Item item = itemRepository.save(Item.builder()
                        .name("name")
                        .description("description")
                        .owner(user)
                        .available(true)
                .build());

        ItemDto updateDto = new ItemDto();
        updateDto.setDescription("new description");
        ItemDtoOut out = itemService.updateItem(user.getId(), item.getId(), updateDto);

        Assertions.assertNotNull(out);
        Assertions.assertEquals("new description", out.getDescription());
        Assertions.assertEquals(item.getName(), out.getName());
        Assertions.assertEquals(item.getAvailable(), out.getAvailable());
    }
}
