package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.entity.Image;
import ru.effectivemobile.service.ImageService;

@RestController
@RequestMapping("/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("{imageName}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable String imageName) {

        Image image = imageService.getImage(imageName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getType()))
                .body(image.getContent());
    }

    @DeleteMapping("{imageName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @PathVariable String imageName) {

        imageService.deleteImage(imageName);
    }
}
