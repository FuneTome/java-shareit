package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceIntegrationTest {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRequestService requestService;
    private final ItemService itemService;
    private final EntityManager entityManager; // добавили

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder()
                .name("User1")
                .email("user1@mail.ru")
                .build());
        user2 = userRepository.save(User.builder()
                .name("User2")
                .email("user2@mail.ru")
                .build());
    }

    @Test
    void getUserRequests_shouldReturnSortedByCreatedDesc() {
        Request oldRequest = Request.builder()
                .description("old")
                .requestor(user1)
                .created(LocalDateTime.now().minusDays(2))
                .build();
        Request newRequest = Request.builder()
                .description("new")
                .requestor(user1)
                .created(LocalDateTime.now().minusHours(1))
                .build();
        requestRepository.saveAll(List.of(oldRequest, newRequest));
        requestRepository.flush();
        entityManager.clear();

        List<ItemRequestDtoOut> result = requestService.getUserRequests(user1.getId()).stream()
                .collect(Collectors.toList());

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getDescription()).isEqualTo("new");
        assertThat(result.get(1).getDescription()).isEqualTo("old");
    }

    @Test
    void getAllRequests_shouldExcludeCurrentUserRequests() {
        Request requestOfUser1 = Request.builder()
                .description("user1 request")
                .requestor(user1)
                .created(LocalDateTime.now())
                .build();
        Request requestOfUser2 = Request.builder()
                .description("user2 request")
                .requestor(user2)
                .created(LocalDateTime.now())
                .build();
        requestRepository.saveAll(List.of(requestOfUser1, requestOfUser2));
        requestRepository.flush();
        entityManager.clear();

        List<ItemRequestDtoOut> resultForUser1 = requestService.getAllRequests(user1.getId()).stream()
                .collect(Collectors.toList());

        assertThat(resultForUser1).hasSize(1);
        assertThat(resultForUser1.getFirst().getDescription()).isEqualTo("user2 request");
    }

    @Test
    void getAllRequests_shouldBeSortedByCreatedDesc() {
        User otherUser = userRepository.save(User.builder()
                .name("Other")
                .email("other@mail.ru")
                .build());
        Request older = Request.builder()
                .description("older")
                .requestor(otherUser)
                .created(LocalDateTime.now().minusDays(1))
                .build();
        Request newer = Request.builder()
                .description("newer")
                .requestor(otherUser)
                .created(LocalDateTime.now().minusHours(1))
                .build();
        requestRepository.saveAll(List.of(older, newer));
        requestRepository.flush();
        entityManager.clear();

        List<ItemRequestDtoOut> result = requestService.getAllRequests(user1.getId()).stream()
                .collect(Collectors.toList());

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getDescription()).isEqualTo("newer");
        assertThat(result.get(1).getDescription()).isEqualTo("older");
    }

    @Test
    void getUserRequests_shouldIncludeItemsWithIdNameAndOwnerId() {
        Request request = Request.builder()
                .description("request with item")
                .requestor(user1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(request);
        requestRepository.flush();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("ItemName");
        itemDto.setDescription("ItemDesc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(request.getId());
        itemService.addItem(user1.getId(), itemDto);
        entityManager.flush();
        entityManager.clear();

        List<ItemRequestDtoOut> result = requestService.getUserRequests(user1.getId()).stream()
                .collect(Collectors.toList());

        assertThat(result).hasSize(1);
        ItemRequestDtoOut requestDto = result.getFirst();

        List<ItemDtoOut> items = new ArrayList<>(requestDto.getItems());
        assertThat(items).hasSize(1);
        assertThat(items.getFirst().getName()).isEqualTo("ItemName");
    }

    @Test
    void getRequest_withNonExistentId_shouldThrowNotFoundException() {
        assertThatThrownBy(() -> requestService.getRequest(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Запрос с id = 999 не найден");
    }

    @Test
    void createRequest_withNonExistentUser_shouldThrowNotFoundException() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("some description");

        assertThatThrownBy(() -> requestService.createRequest(dto, 999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Юзер с id = 999 не найден");
    }

    @Test
    void addItem_withExistingRequestId_shouldLinkItemToRequest() {
        Request request = Request.builder()
                .description("existing request")
                .requestor(user1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(request);
        requestRepository.flush();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(request.getId());

        itemService.addItem(user1.getId(), itemDto);
        entityManager.flush();
        entityManager.clear();

        ItemRequestDtoOut requestDto = requestService.getRequest(request.getId());
        List<ItemDtoOut> items = new ArrayList<>(requestDto.getItems());
        assertThat(items).hasSize(1);
        assertThat(items.getFirst().getName()).isEqualTo("Item");
    }

    @Test
    void addItem_withNonExistentRequestId_shouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(999L);

        assertThatThrownBy(() -> itemService.addItem(user1.getId(), itemDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Запрос с id = 999 не найден");
    }
}