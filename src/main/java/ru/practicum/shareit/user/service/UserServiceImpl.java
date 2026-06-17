package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto getUser(long id) {
        checkUserExists(id);
        User user = userRepository.getUserById(id);
        return UserMapper.toDto(user);
    }

    public UserDto addUser(UserDto userDto) {
        checkEmailExists(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        User saved = userRepository.addUser(user);
        return UserMapper.toDto(saved);
    }

    public UserDto updateUser(UserUpdateDto userUpdate, long id) {
        checkUserExists(id);
        User updateUser = UserMapper.toUser(userUpdate);
        return UserMapper.toDto(userRepository.updateUser(updateUser, id));
    }

    public void deleteUser(long id) {
        checkUserExists(id);
        userRepository.deleteUser(id);
    }

    private void checkUserExists(long userId) {
        if (!userRepository.existById(userId)) {
            throw new NotFoundException("Юзер с id = " + userId + " не найден");
        }
    }

    private void checkEmailExists(String email) {
        if (userRepository.existByEmail(email)) {
            throw new ConflictException("Почта = " + email + " уже используется");
        }
    }
}
