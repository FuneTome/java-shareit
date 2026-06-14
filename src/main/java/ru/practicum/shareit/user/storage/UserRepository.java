package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User getUserById(long id);

    User updateUser(User user, long id);

    User addUser(User user);

    void deleteUser(long id);

    boolean existById(long id);

    boolean existByEmail(String email);
}
