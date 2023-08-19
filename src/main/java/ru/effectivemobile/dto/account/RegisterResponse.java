package ru.effectivemobile.dto.account;

public record RegisterResponse(
        Long id,
        String login,
        String email
) {
}
