package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByItem_Owner_Id(long ownerId);

    List<Comment> findByItemId(Long itemId);
}