package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

/**
 * Сущность представляющая изображение для пользователя.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "avatars")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(columnDefinition = "bytea")
    private byte[] data;
}
