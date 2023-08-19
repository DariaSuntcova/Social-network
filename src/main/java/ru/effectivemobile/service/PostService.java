package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.effectivemobile.dto.post.PostDTO;
import ru.effectivemobile.dto.post.PostRequest;
import ru.effectivemobile.entity.Post;
import ru.effectivemobile.entity.User;
import ru.effectivemobile.exceptions.InputDataException;
import ru.effectivemobile.repository.PostRepository;
import ru.effectivemobile.util.ImageUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.Spliterator;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final ImageUtil imageUtil;

    public PostDTO addNewPost(String login, PostRequest postRequest) {
        User user = userService.loadUserByLogin(login);

        if (postRequest.title() == null || postRequest.title().isEmpty()) {
            throw new InputDataException("Заголовок не может быть пустым");
        }
        if (postRequest.text() == null || postRequest.text().isEmpty()) {
            throw new InputDataException("Текст не может быть пустым");
        }

        Set<String> imageUrlList = new HashSet<>();
        if (postRequest.images() != null) {
            Spliterator<MultipartFile> spliterator = postRequest.images().spliterator();
            while (spliterator.tryAdvance(imageFile -> imageUrlList.add(imageUtil.resizeAndSaveFile(imageFile)))) ;
        }
        Post post = postRepository.save(
                Post.builder()
                        .title(postRequest.title())
                        .text(postRequest.text())
                        .author(user)
                        .imageUrlList(imageUrlList)
                        .build());
        return new PostDTO(
                post.getId(),
                post.getData(),
                post.getAuthor().getLogin(),
                post.getTitle(),
                post.getText(),
                post.getImageUrlList());
    }

}
