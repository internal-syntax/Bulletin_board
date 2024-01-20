package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad,Integer> {
    List<Ad> findAdByUser(User user);
}
