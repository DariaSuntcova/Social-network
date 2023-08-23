package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.dto.account.ChangeEmailRequest;
import ru.effectivemobile.dto.account.ChangePasswordRequest;
import ru.effectivemobile.dto.account.RegisterRequest;
import ru.effectivemobile.dto.account.RegisterResponse;
import ru.effectivemobile.service.AccountService;

import java.security.Principal;


@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse addNewUser(
            @RequestBody RegisterRequest request) {
        return accountService.addNewUser(request);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(
            Principal principal,
            @RequestBody ChangePasswordRequest changePasswordDto) {
        accountService.setPassword(principal.getName(), changePasswordDto);
    }

    @PutMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(
            Principal principal,
            @RequestBody ChangeEmailRequest changeEmailRequest) {
        accountService.setEmail(principal.getName(), changeEmailRequest);
    }
}
