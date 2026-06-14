package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private Long id = 1L;

    public User getUserById(long id) {
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

    public User updateUser(User updateUser, long id) {
        User oldUser = getUserById(id);
        if (updateUser.getName() != null) {
            oldUser.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            if (!updateUser.getEmail().equals(oldUser.getEmail())) {
                if (existByEmail(updateUser.getEmail())) {
                    throw new ConflictException("Почта = " + updateUser.getEmail() + " уже используется");
                }
                oldUser.setEmail(updateUser.getEmail());
            }
        }
        return oldUser;
    }

    public void deleteUser(long id) {
        User deleted = getUserById(id);
        users.remove(deleted);
    }

    public boolean existById(long id) {
        return users.stream().anyMatch(user -> user.getId() == id);
    }

    public boolean existByEmail(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
