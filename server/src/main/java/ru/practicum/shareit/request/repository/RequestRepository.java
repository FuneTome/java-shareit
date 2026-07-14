package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.Request;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @EntityGraph(attributePaths = "items")
    Collection<Request> findByRequestor_IdOrderByCreatedDesc(long requestorId);

    @EntityGraph(attributePaths = "items")
    @Query("select r from Request r where r.requestor.id != ?1 order by r.created desc")
    Collection<Request> findAllWithoutUserId(long userId);
}
