package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceIntegrationTest {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRequestService requestService;

    @Test
    public void getAllRequestsTest() {
        User user = userRepository.save(User.builder()
                .name("name")
                .email("e@mail.ru")
                .build());

        Request one = Request.builder()
                .created(LocalDateTime.now())
                .requestor(user)
                .description("description_one")
                .build();

        Request two = Request.builder()
                .created(LocalDateTime.now())
                .requestor(user)
                .description("description_two")
                .build();

        requestRepository.saveAll(List.of(one, two));

        List<ItemRequestDtoOut> result = requestService.getAllRequests().stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        List<String> expectedDescriptions = List.of("description_one", "description_two");
        List<String> actualDescriptions = result.stream()
                .map(ItemRequestDtoOut::getDescription)
                .sorted()
                .toList();

        Assertions.assertEquals(expectedDescriptions.stream().sorted().toList(), actualDescriptions);
    }
}
