package ru.effectivemobile.dto.account;

public record ChangePasswordRequest(
        String password,
        String passwordConfirm) {

}
