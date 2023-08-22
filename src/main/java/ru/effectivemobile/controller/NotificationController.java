package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.dto.notification.NotificationsDto;
import ru.effectivemobile.service.NotificationService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/all")
    public List<NotificationsDto> getAllNotification(
            Principal principal,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        return notificationService.getAllNotification(principal.getName(), pageNumber, pageSize);
    }

    @GetMapping()
    public List<NotificationsDto> getNewNotification(
            Principal principal) {

        return notificationService.getNewNotification(principal.getName());
    }

}
