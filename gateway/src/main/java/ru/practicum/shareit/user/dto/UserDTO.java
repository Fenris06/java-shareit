package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;


@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDTO {
    private Long id;
    private String name;
    @Email(message = "Invalid email")
    private String email;
}