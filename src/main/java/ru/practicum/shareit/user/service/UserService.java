package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {
    UserDto getUser(long id);

    UserDto addUser(UserDto user);

    UserDto updateUser(UserUpdateDto userUpdate, long id);

    void deleteUser(long id);
}
