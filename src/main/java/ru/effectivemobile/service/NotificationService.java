package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.effectivemobile.dto.notification.NotificationsDto;
import ru.effectivemobile.entity.Notification;
import ru.effectivemobile.entity.User;
import ru.effectivemobile.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    public List<NotificationsDto> getNewNotification(String login) {
        User user = userService.loadUserByLogin(login);
        boolean itsRead = false;
        List<Notification> notifications = notificationRepository
                .findByTargetUserAndItsRead(user, itsRead);
        return createListNotificationsDto(notifications);
    }

    public List<NotificationsDto> getAllNotification(String login, int pageNumber, int pageSize) {
        User user = userService.loadUserByLogin(login);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Notification> notifications = notificationRepository.findByTargetUserByDataDesc(user, pageable);

        return createListNotificationsDto(notifications);
    }

    private List<NotificationsDto> createListNotificationsDto(List<Notification> notifications) {
        return notifications.stream().map(this::createNotificationsDto).collect(Collectors.toList());
    }

    private NotificationsDto createNotificationsDto(Notification notification) {
        return new NotificationsDto(
                notification.getAuthor().getLogin(),
                notification.getData(),
                notification.getNotificationType()
        );
    }

    public void addNewNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}
