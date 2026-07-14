package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    @NotNull
    @JsonProperty("itemId")
    private Long itemId;

    @NotNull
    @FutureOrPresent
    @JsonProperty("start")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull
    @Future
    @JsonProperty("end")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
}
