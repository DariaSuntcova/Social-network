package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.dto.post.PostDTO;
import ru.effectivemobile.dto.post.PostRequest;
import ru.effectivemobile.service.PostService;

import java.security.Principal;

@RestController
@RequestMapping("/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostDTO addNewPost(Principal principal, @ModelAttribute PostRequest postRequest) {
        return postService.addNewPost(principal.getName(), postRequest);
    }
}
