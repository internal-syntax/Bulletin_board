package ru.skypro.homework.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity (name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "price")
    private int price;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "image_id")
    private AdImage adImage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
