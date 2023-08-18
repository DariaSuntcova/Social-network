package ru.effectivemobile.dto.account;

public record RegistrationRequest(
        String login,
        String password,
        String passwordConfirm,
        String email
) {

}
