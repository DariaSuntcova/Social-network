package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.effectivemobile.dto.friend.FriendsResponse;
import ru.effectivemobile.entity.*;
import ru.effectivemobile.exceptions.FriendshipException;
import ru.effectivemobile.repository.FriendshipRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserService userService;
    private final FriendshipRepository friendshipRepository;

    private final NotificationService notificationService;

    public void addFriends(String mainUserLogin, long targetUserId) {
        User mainUser = userService.loadUserByLogin(mainUserLogin);
        User targetUser = userService.loadUserById(targetUserId);

        if (mainUser.getId() == targetUserId) {
            throw new FriendshipException("Нельзя добавить в друзья самого себя");
        }

        // Проверяем новый это запрос дружбы или принимаем заявку от таргета
        Optional<Friendship> friendshipOptional = friendshipRepository.findByMainUserAndTargetUser(targetUser, mainUser);

        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();
            friendship.setStatus(FriendshipStatus.FRIEND);
            friendshipRepository.save(Friendship.builder()
                    .status(FriendshipStatus.FRIEND)
                    .mainUser(mainUser)
                    .targetUser(targetUser)
                    .build());
        } else {
            friendshipRepository.save(Friendship.builder()
                    .status(FriendshipStatus.SUBSCRIBED)
                    .mainUser(mainUser)
                    .targetUser(targetUser)
                    .build());

            notificationService.addNewNotification(Notification.builder()
                    .author(mainUser)
                    .targetUser(targetUser)
                    .itsRead(false)
                    .data(new Date())
                    .notificationType(NotificationType.FRIEND_REQUEST)
                    .build());
        }
    }

    public List<FriendsResponse> getAllWithPageable(String login, FriendshipStatus status, int pageNumber, int pageSize) {
        User user = userService.loadUserByLogin(login);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<FriendsResponse> listFriends = new ArrayList<>();

        List<Friendship> friendshipList = friendshipRepository.findByMainUserAndStatus(user, status, pageable);
        for (Friendship friendship : friendshipList) {
            User targetUser = friendship.getTargetUser();
            listFriends.add(new FriendsResponse(
                    targetUser.getLogin()));
        }
        return listFriends;
    }


    public void deleteFriends(String mainUserLogin, long targetUserId) {
        User mainUser = userService.loadUserByLogin(mainUserLogin);

        User targetUser = userService.loadUserById(targetUserId);
        Friendship friendship = friendshipRepository.findByMainUserAndTargetUser(mainUser, targetUser)
                .orElseThrow(() -> new FriendshipException("Этот пользователь не является Вашим другом"));

        friendshipRepository.delete(friendship);

        Optional<Friendship> friendshipSubscribed = friendshipRepository.findByMainUserAndTargetUser(targetUser, mainUser);
        if (friendshipSubscribed.isPresent()) {
            friendship = friendshipSubscribed.get();
            friendship.setStatus(FriendshipStatus.SUBSCRIBED);
            friendshipRepository.save(friendship);
        }
    }

    public List<User> getAll(String login, FriendshipStatus status) {
        User user = userService.loadUserByLogin(login);

        List<User> listFriends = new ArrayList<>();

        List<Friendship> friendshipList = friendshipRepository.findAllByMainUserAndStatus(user, status);
        for (Friendship friendship : friendshipList) {
            listFriends.add(friendship.getTargetUser());
        }
        return listFriends;
    }

    protected Optional<Friendship> checkFriend(User mainUser, User targetUser) {
        return friendshipRepository.findByMainUserAndTargetUser(mainUser, targetUser);
    }

}
