package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

/**
 * Сущность представляющая изображение для объявления.
 */
@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "image_path")
    private String imagePath;
}
