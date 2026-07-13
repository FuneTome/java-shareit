package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceIntegrationTest {
    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    void updateUserTest() {
        User user = userRepository.save(User.builder()
                .name("name")
                .email("e@mail.ru")
                .build());

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("m@el.ee");
        UserDto updatedUser = userService.updateUser(userUpdateDto, user.getId());

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(updatedUser.getName(), user.getName());
        Assertions.assertEquals(updatedUser.getEmail(), userUpdateDto.getEmail());
    }
}
