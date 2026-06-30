package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findByBooker_Id(long bookerId);

    Collection<Booking> findByBooker_IdAndEndDateIsBefore(long bookerId, LocalDateTime end);

    Collection<Booking> findByBooker_IdAndStartDateIsAfter(long bookerId, LocalDateTime start);

    Collection<Booking> findByBooker_IdAndStatus(long bookerId, BookingStatus status);

    @Query("select b from Booking b where b.booker.id = ?1 and ?2 between b.startDate and b.endDate")
    Collection<Booking> findByBetween(long bookerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)")
    Collection<Booking> findByOwnerIdAll(long ownerId);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and ?2 between b.startDate and b.endDate")
    Collection<Booking> findByOwnerIdCurrent(long ownerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and ?2 > b.endDate")
    Collection<Booking> findByOwnerIdPast(long ownerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and ?2 < b.startDate")
    Collection<Booking> findByOwnerIdFuture(long ownerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and b.status = ?2")
    Collection<Booking> findByOwnerIdAndStatus(long ownerId, BookingStatus status);

    Optional<Booking> findByBookerIdAndItemIdAndStatusAndEndDateBefore(
            Long bookerId, Long itemId, BookingStatus status, LocalDateTime endDate);

    boolean existsByBookerIdAndItemId(Long bookerId, Long itemId);

    Collection<Booking> findAllByItem_Owner_Id(long ownerId);

    List<Booking> findByItemId(Long itemId);
}
