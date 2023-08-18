package ru.effectivemobile.dto.notification;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
@ApiResponse
public record NotificationListResponse(
        List<NotificationsDto> data
) {
}
