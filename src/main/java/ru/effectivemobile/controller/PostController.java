package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.dto.post.PostDTO;
import ru.effectivemobile.dto.post.PostRequest;
import ru.effectivemobile.service.PostService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostDTO addNewPost(
            Principal principal,
            @ModelAttribute PostRequest postRequest) {
        return postService.addNewPost(principal.getName(), postRequest);
    }

    @GetMapping
    public List<PostDTO> getAllPost(
            Principal principal,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return postService.getAllPost(principal.getName(), pageNumber, pageSize);
    }

    @GetMapping("{id}")
    public PostDTO getPostById(
            @PathVariable long id) {
        return postService.getPostById(id);
    }


    @PutMapping("{id}")
    public PostDTO changePostById(
            Principal principal,
            @PathVariable long id,
            @ModelAttribute PostRequest postRequest) {
        return postService.changePostById(principal.getName(), id, postRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(
            Principal principal,
            @PathVariable long id) {
        postService.deletePostById(principal.getName(), id);
    }
}
