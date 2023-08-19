package ru.effectivemobile.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.effectivemobile.entity.Image;

import java.util.Optional;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {

    Optional<Image> findImageByImageName(String imageName);

    void deleteByImageName(String imageName);
}