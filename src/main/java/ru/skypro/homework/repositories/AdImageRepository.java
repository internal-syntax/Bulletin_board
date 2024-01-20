package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdImage;

@Repository
public interface AdImageRepository extends JpaRepository<AdImage,Integer> {
}
