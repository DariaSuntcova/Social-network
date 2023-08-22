package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effectivemobile.dto.dialog.DialogResponse;
import ru.effectivemobile.entity.*;
import ru.effectivemobile.exceptions.DialogException;
import ru.effectivemobile.repository.DialogRepository;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DialogService {
    private final NotificationService notificationService;
    private final FriendService friendService;
    private final UserService userService;
    private final DialogRepository dialogRepository;

    public DialogResponse createDialog(String mainUserLogin, long targetUserId) {
        User mainUser = userService.loadUserByLogin(mainUserLogin);

        User targetUser = userService.loadUserById(targetUserId);

        if (mainUser.getId() == targetUserId) {
            throw new DialogException("Нельзя предложить диалог самому себе");
        }

        Optional<Friendship> friendshipOptional = friendService.checkFriend(mainUser, targetUser);

        if (friendshipOptional.map(Friendship::getStatus).orElse(null) == FriendshipStatus.SUBSCRIBED) {
            throw new DialogException("Нельзя предложить диалог пользователю, который не является Вашим другом");
        }
        dialogRepository.save(Dialog.builder()
                .mainUser(mainUser)
                .targetUser(targetUser)
                .build());

        notificationService.addNewNotification(Notification.builder()
                .author(mainUser)
                .targetUser(targetUser)
                .notificationType(NotificationType.DIALOG)
                .itsRead(false)
                .data(new Date())
                .build()
        );
        //проверяем новый это диалог или согласие
        Optional<Dialog> dialogOptional = dialogRepository.findByMainUserAndTargetUser(targetUser, mainUser);

        if (dialogOptional.isPresent()) {
            return new DialogResponse("Диалог открыт");
        } else {
            return new DialogResponse("Диалог предложен");
        }
    }

    public void deleteDialog(String mainUserLogin, long targetUserId) {
        User mainUser = userService.loadUserByLogin(mainUserLogin);

        User targetUser = userService.loadUserById(targetUserId);

        dialogRepository.delete(Dialog.builder()
                .mainUser(mainUser)
                .targetUser(targetUser)
                .build());
        dialogRepository.delete(Dialog.builder()
                .mainUser(targetUser)
                .targetUser(mainUser)
                .build());
    }
}
