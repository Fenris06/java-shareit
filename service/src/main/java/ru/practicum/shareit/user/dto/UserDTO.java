package ru.practicum.shareit.user.dto;

import lombok.*;





@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDTO {
    private Long id;
    private String name;
    private String email;
}
