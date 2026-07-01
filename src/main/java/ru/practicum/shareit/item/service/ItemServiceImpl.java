package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Collection<ItemDtoOut> getAllItems(long ownerId) {
        checkUserExistAndReturn(ownerId);

        Collection<Item> items = itemRepository.findAllByOwnerId(ownerId); //все вещи конкретного владельца
        Collection<Booking> bookings = bookingRepository.findAllByItem_Owner_Id(ownerId); //все бронирования вещей данного владельца
        Collection<Comment> comments = commentRepository.findAllByItem_Owner_Id(ownerId); //все комментарии к вещам данного владельца

        Map<Long, List<CommentDtoOut>> cms = new HashMap<>();
        Map<Long, List<BookingDtoOut>> bks = new HashMap<>();

        for (Item i : items) {
            Long id = i.getId();
            cms.put(id, comments.stream()
                            .filter(c -> Objects.equals(c.getItem().getId(), id))
                            .map(CommentMapper::toOut)
                            .toList());

            bks.put(id, bookings.stream()
                            .filter(b -> Objects.equals(b.getItem().getId(), id))
                            .map(BookingMapper::toOut)
                            .toList());
        }

        return items.stream()
                .map(item -> ItemMapper.toOut(item,
                        getLastBooking(bks.get(item.getId()), LocalDateTime.now()),
                        getNextBooking(bks.get(item.getId()), LocalDateTime.now()),
                        cms.get(item.getId())))
                .toList();
    }

    @Override
    public ItemDtoOut getItemById(long userId, long itemId) {
        Item item = checkItemExistAndReturn(itemId);

        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentDtoOut> commentDto = comments.stream()
                .map(CommentMapper::toOut)
                .collect(Collectors.toList());

        BookingDtoOut lastBooking = null;
        BookingDtoOut nextBooking = null;

        if (item.getOwner().getId() == userId) {
            List<Booking> bookings = bookingRepository.findByItemId(itemId);
            List<BookingDtoOut> bookingDto = bookings.stream()
                    .map(BookingMapper::toOut)
                    .collect(Collectors.toList());
            lastBooking = getLastBooking(bookingDto, LocalDateTime.now());
            nextBooking = getNextBooking(bookingDto, LocalDateTime.now());
        }

        return ItemMapper.toOut(item, lastBooking, nextBooking, commentDto);
    }

    @Override
    public Collection<ItemDtoOut> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        Collection<Item> items = itemRepository.search(text);
        return items.stream()
                .map(ItemMapper::toOut)
                .toList();
    }

    @Override
    public ItemDtoOut addItem(long ownerId, ItemDto newItem) {
        User user = checkUserExistAndReturn(ownerId);
        newItem.setOwner(user);
        Item item = ItemMapper.toItem(newItem);
        return ItemMapper.toOut(itemRepository.save(item));
    }

    @Override
    public ItemDtoOut updateItem(long userId, long itemId, ItemDto itemUpdate) {
        Item existingItem = checkItemExistAndReturn(itemId);
        checkUserExistAndReturn(userId);
        if (existingItem.getOwner().getId() != userId) {
            throw new ForbiddenException("Юзер с id = " + userId + " не является владельцем вещи");
        }
        if (itemUpdate.getName() != null && !itemUpdate.getName().isBlank()) {
            existingItem.setName(itemUpdate.getName());
        }
        if (itemUpdate.getDescription() != null && !itemUpdate.getDescription().isBlank()) {
            existingItem.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getAvailable() != null) {
            existingItem.setAvailable(itemUpdate.getAvailable());
        }
        return ItemMapper.toOut(itemRepository.save(existingItem));
    }

    @Override
    public CommentDtoOut createComment(long authorId, long itemId, CommentDto comment) {
        User user = checkUserExistAndReturn(authorId);
        Item item = checkItemExistAndReturn(itemId);
        LocalDateTime now = LocalDateTime.now();

        Optional<Booking> approvedStarted = bookingRepository
                .findApprovedAndStarted(authorId, itemId, now);

        if (approvedStarted.isPresent()) {
            Comment cm = CommentMapper.toComment(comment, user, item);
            return CommentMapper.toOut(commentRepository.save(cm));
        }

        boolean hasAny = bookingRepository.existsByBookerIdAndItemId(authorId, itemId);
        if (!hasAny) {
            throw new NotFoundException("Юзер с id = " + authorId + " не бронировал данную вещь");
        } else {
            throw new BadRequestException("Невозможно оставить комментарий: бронирование не одобрено или ещё не началось");
        }
    }

    private User checkUserExistAndReturn(long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Юзер с id = " + userId + " не найден"));
    }

    private Item checkItemExistAndReturn(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Предмет с id = " + itemId + " не найден"));
    }

    private BookingDtoOut getLastBooking(Collection<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.stream()
                .filter(bookingDTO -> !bookingDTO.getStartDate().isAfter(time))
                .reduce((booking1, booking2) ->
                        booking1.getStartDate().isAfter(booking2.getStartDate()) ? booking1 : booking2)
                .orElse(null);
    }

    private BookingDtoOut getNextBooking(Collection<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.stream()
                .filter(bookingDTO -> bookingDTO.getStartDate().isAfter(time))
                .findFirst()
                .orElse(null);
    }
}
