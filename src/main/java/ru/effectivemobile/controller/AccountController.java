package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.dto.account.*;
import ru.effectivemobile.dto.notification.NotificationListResponse;
import ru.effectivemobile.service.AccountService;

import java.security.Principal;


@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWithToken addNewUser(@RequestBody RegistrationRequest request) {
        return accountService.addNewUser(request);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(Principal principal, @RequestBody ChangePasswordRequest changePasswordDto) {
        accountService.setPassword(principal.getName(), changePasswordDto);
    }

    @PutMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(Principal principal, @RequestBody ChangeEmailRequest changeEmailDto) {
        accountService.setEmail(principal.getName(), changeEmailDto);
    }


    @GetMapping("/notifications")
    @ResponseStatus(HttpStatus.OK)
    public NotificationListResponse getNotifications(Principal principal) {
        return accountService.getNotification(principal.getName());
    }
}
