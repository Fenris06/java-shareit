package ru.practicum.shareit.comment.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;


import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(name = "author_id")
    private Long author;
    @Column(name = "create_date")
    private LocalDateTime create;
}
