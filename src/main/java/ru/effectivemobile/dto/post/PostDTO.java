package ru.effectivemobile.dto.post;

import java.util.Date;
import java.util.Set;


public record PostDTO(
        long id,
        Date data,
        String authorName,
        String title,
        String text,
        Set<String> imageUrlList) {
}
