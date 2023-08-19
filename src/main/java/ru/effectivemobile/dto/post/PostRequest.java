package ru.effectivemobile.dto.post;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostRequest(
        String title,
        String text,
        List<MultipartFile> images
) {
}
