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
        User user = userRepository.findById(id).orElseThrow();
        return UserMapper.toDto(user);
    }

    public UserDto addUser(UserDto userDto) {
        checkEmailExists(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    public UserDto updateUser(UserUpdateDto userUpdate, long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Юзер с id = " + id + " не найден"));

        if (userUpdate.getName() != null && !userUpdate.getName().isBlank()) {
            existingUser.setName(userUpdate.getName());
        }
        if (userUpdate.getEmail() != null && !userUpdate.getEmail().isBlank()) {
            if (!userUpdate.getEmail().equals(existingUser.getEmail())) {
                checkEmailExists(userUpdate.getEmail());
            }
            existingUser.setEmail(userUpdate.getEmail());
        }
        return UserMapper.toDto(userRepository.save(existingUser));
    }

    public void deleteUser(long id) {
        checkUserExists(id);
        userRepository.deleteById(id);
    }

    private void checkUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Юзер с id = " + userId + " не найден");
        }
    }

    private void checkEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Почта = " + email + " уже используется");
        }
    }
}
