package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBookingDTO;
import ru.practicum.shareit.user.dto.UserBookingDTO;

import java.time.LocalDateTime;


import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class BookingAnswerDTOTest {
    @Autowired
    private JacksonTester<BookingAnswerDTO> jacksonTester;

    @SneakyThrows
    @Test
    void bookingAnswerDto() {
        ItemBookingDTO item = new ItemBookingDTO();
        item.setId(1L);
        item.setName("Drill");

        UserBookingDTO user = new UserBookingDTO();
        user.setId(2L);

        BookingAnswerDTO booking = new BookingAnswerDTO();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2023, 5, 25, 12, 30, 0));
        booking.setEnd(LocalDateTime.of(2023, 5, 25, 1, 30, 0));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(item);
        booking.setBooker(user);
        JsonContent<BookingAnswerDTO> content = jacksonTester.write(booking);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(Math.toIntExact(booking.getId()));
        assertThat(content).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString() + ":00");
        assertThat(content).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString() + ":00");
        assertThat(content).extractingJsonPathStringValue("$.status").isEqualTo(booking.getStatus().toString());
        assertThat(content).extractingJsonPathNumberValue("$.item.id").isEqualTo(Math.toIntExact(booking.getItem().getId()));
        assertThat(content).extractingJsonPathNumberValue("$.booker.id").isEqualTo(Math.toIntExact(booking.getBooker().getId()));
    }
}

