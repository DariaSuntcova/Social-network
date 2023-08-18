package ru.effectivemobile.dto.auth;

public record AuthRequest(
        String login,
        String password
) {
}
