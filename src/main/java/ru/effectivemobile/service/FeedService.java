package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.effectivemobile.dto.post.PostDTO;
import ru.effectivemobile.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final PostService postService;
    private final FriendService friendService;

    public List<PostDTO> getFeed(String login, int pageNumber, int pageSize) {
        // получаем всех на кого подписан пользователь
        List<User> listFriends = friendService.getAll(login);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return postService.createListPostDTO(listFriends, pageable);
    }
}
