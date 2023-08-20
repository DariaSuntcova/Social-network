package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.effectivemobile.dto.post.PostDTO;
import ru.effectivemobile.dto.post.PostRequest;
import ru.effectivemobile.entity.Post;
import ru.effectivemobile.entity.User;
import ru.effectivemobile.exceptions.ForbiddenException;
import ru.effectivemobile.exceptions.InputDataException;
import ru.effectivemobile.exceptions.PostNotFoundException;
import ru.effectivemobile.repository.PostRepository;
import ru.effectivemobile.util.ImageUtil;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final ImageUtil imageUtil;
    private final ImageService imageService;

    public PostDTO addNewPost(String login, PostRequest postRequest) {
        User user = userService.loadUserByLogin(login);

        if (postRequest.title() == null || postRequest.title().isEmpty()) {
            throw new InputDataException("Заголовок не может быть пустым");
        }

        Set<String> imageUrlList = new HashSet<>();
        if (postRequest.images() != null) {
            Spliterator<MultipartFile> spliterator = postRequest.images().spliterator();
            while (spliterator.tryAdvance(imageFile -> imageUrlList.add(imageUtil.resizeAndSaveFile(imageFile)))) ;
        }
        Post post = postRepository.save(Post.builder()
                .title(postRequest.title())
                .data(new Date())
                .text(postRequest.text())
                .author(user)
                .imageUrlList(imageUrlList)
                .build());

        return createPostDTO(post);
    }

    public List<PostDTO> getAllPost(String login, int pageNumber, int pageSize) {
        User user = userService.loadUserByLogin(login);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Post> postList = postRepository.findByAuthorOrderByDataDesc(user, pageable);

        List<PostDTO> postDTOList = new ArrayList<>();

        for (Post post : postList) {
            postDTOList.add(createPostDTO(post));
        }
        return postDTOList;
    }

    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        return createPostDTO(post);
    }

    public PostDTO changePostById(String login, long id, PostRequest postRequest) {
        User user = userService.loadUserByLogin(login);

        Post oldPost = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        // теоретически на фронте должна отсутствовать возможность менять чужие посты,
        // но на всякий случай пусть будет и тут =)
        if (!oldPost.getAuthor().getLogin().equals(login)) {
            throw new ForbiddenException("Нельзя менять чужие посты");
        }

        Set<String> imageUrlList = new HashSet<>();

        if (postRequest.images() != null) {
            Set<String> oldImageUrlList = oldPost.getImageUrlList();
            oldImageUrlList.forEach(imageName -> imageService.deleteImage(imageName.substring(imageName.lastIndexOf("/") + 1)));

            Spliterator<MultipartFile> spliterator = postRequest.images().spliterator();
            while (spliterator.tryAdvance(imageFile -> imageUrlList.add(imageUtil.resizeAndSaveFile(imageFile)))) ;
        }

        Post post = postRepository.save(Post.builder()
                .title(postRequest.title())
                .data(new Date())
                .text(postRequest.text())
                .author(user)
                .imageUrlList(imageUrlList)
                .build());

        return createPostDTO(post);
    }

    public void deletePostById(String login, long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        // теоретически на фронте должна отсутствовать возможность удалять чужие посты,
        // но на всякий случай пусть будет и тут =)
        if (!post.getAuthor().getLogin().equals(login)) {
            throw new ForbiddenException("Нельзя удалять чужие посты");
        }
        postRepository.deleteById(id);

    }

    private PostDTO createPostDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getData(),
                post.getAuthor().getLogin(),
                post.getTitle(),
                post.getText(),
                post.getImageUrlList());
    }


}
