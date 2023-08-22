package ru.effectivemobile.dto.account;

public record RegisterRequest(

        String login,

        String password,

        String passwordConfirm,

        String email
) {

}
