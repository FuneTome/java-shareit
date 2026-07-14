package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findByBooker_IdOrderByStartDateDesc(long bookerId);

    Collection<Booking> findByBooker_IdAndEndDateIsBeforeOrderByStartDateDesc(long bookerId, LocalDateTime end);

    Collection<Booking> findByBooker_IdAndStartDateIsAfterOrderByStartDateDesc(long bookerId, LocalDateTime start);

    Collection<Booking> findByBooker_IdAndStatusOrderByStartDateDesc(long bookerId, BookingStatus status);

    @Query("select b from Booking b where b.booker.id = ?1 and ?2 between b.startDate and b.endDate order by b.startDate desc")
    Collection<Booking> findByBetween(long bookerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1) order by b.startDate desc")
    Collection<Booking> findByOwnerIdAll(long ownerId);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and ?2 between b.startDate and b.endDate order by b.startDate desc")
    Collection<Booking> findByOwnerIdCurrent(long ownerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and ?2 > b.endDate order by b.startDate desc")
    Collection<Booking> findByOwnerIdPast(long ownerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and ?2 < b.startDate order by b.startDate desc")
    Collection<Booking> findByOwnerIdFuture(long ownerId, LocalDateTime time);

    @Query("select b from Booking b where b.item.id in " +
            "(select i.id from Item i where i.owner.id = ?1)" +
            "and b.status = ?2 order by b.startDate desc")
    Collection<Booking> findByOwnerIdAndStatus(long ownerId, BookingStatus status);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.booker.id = ?1 AND b.item.id = ?2")
    boolean existsByBookerIdAndItemId(long bookerId, long itemId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.item.id = ?2 " +
            "AND b.startDate < ?3")
    Optional<Booking> findApprovedAndStarted(Long bookerId, Long itemId, LocalDateTime now);

    Collection<Booking> findAllByItem_Owner_Id(long ownerId);

    List<Booking> findByItemId(Long itemId);
}
