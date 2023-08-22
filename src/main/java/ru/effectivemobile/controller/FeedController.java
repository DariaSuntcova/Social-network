package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.dto.post.PostDTO;
import ru.effectivemobile.service.FeedService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping
    public List<PostDTO> getFeed(
            Principal principal,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        return feedService.getFeed(principal.getName(), pageNumber, pageSize);
    }
}
