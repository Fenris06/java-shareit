package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;



/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private Long id;
    private String name;
    @Email(message = "Invalid email")
    private String email;
}
