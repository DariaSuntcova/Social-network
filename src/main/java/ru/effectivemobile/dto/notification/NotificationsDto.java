package ru.effectivemobile.dto.notification;

import ru.effectivemobile.entity.NotificationType;

import java.util.Date;

public record NotificationsDto(
        String authorLogin,
        Date date,
        NotificationType notificationType
) {

}
