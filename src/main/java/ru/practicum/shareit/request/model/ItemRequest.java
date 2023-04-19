package ru.practicum.shareit.request.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestorId;
    private Instant created;
}
