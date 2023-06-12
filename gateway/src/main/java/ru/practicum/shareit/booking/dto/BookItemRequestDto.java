package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    private long itemId;
    @NotNull(message = "Start time not add")
    @FutureOrPresent(message = "start time is in the past")
    private LocalDateTime start;
    @NotNull(message = "End time not add")
    @Future(message = "End time in the past or present")
    private LocalDateTime end;
}
