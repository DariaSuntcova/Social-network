package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.dto.DialogResponse;
import ru.effectivemobile.service.DialogService;

import java.security.Principal;

@RestController
@RequestMapping("/v1/dialogs")
@RequiredArgsConstructor
public class DialogController {

    private final DialogService dialogService;

    @PostMapping("/{id}")
    public DialogResponse createDialog(
            Principal principal,
            @PathVariable int id) {
        return dialogService.createDialog(principal.getName(), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDialog(
            Principal principal,
            @PathVariable int id) {
        dialogService.deleteDialog(principal.getName(), id);
    }
}
