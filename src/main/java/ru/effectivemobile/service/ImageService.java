package ru.effectivemobile.service;

import org.springframework.stereotype.Service;
import ru.effectivemobile.entity.Image;
import ru.effectivemobile.repository.ImageRepository;
import ru.effectivemobile.exceptions.ImageNotFoundException;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImage(String imageName) {
        return imageRepository.findImageByImageName(imageName).orElseThrow(ImageNotFoundException::new);
    }

    public void deleteImage(String imageName) {
        imageRepository.deleteByImageName(imageName);
    }
}