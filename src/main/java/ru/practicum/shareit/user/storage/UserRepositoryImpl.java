package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();
    Long id = 1L;

    public User getUserById (long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public User addUser(User user) {
        user.setId(id++);
        users.add(user);
        return user;
    }

    public User updateUser(User user) {
        int index = IntStream.range(0, users.size())
                .filter(i -> users.get(i).getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(""));
        users.set(index, user);
        return user;
    }

    public void deleteUser (long id) {
        User deleted = getUserById(id);
        users.remove(deleted);
    }

    public boolean existById (long id) {
        return users.stream().anyMatch(user -> user.getId().equals(id));
    }

    public boolean existByEmail (String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
