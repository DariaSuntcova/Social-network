package ru.effectivemobile.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.effectivemobile.dto.account.RegisterResponse;
import ru.effectivemobile.dto.account.RegisterRequest;
import ru.effectivemobile.service.AccountService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Test
    void testAddNewUser() {
        AccountService accountService = mock(AccountService.class);
        when(accountService.addNewUser(Mockito.any())).thenReturn(new RegisterResponse(null,null,null));
        AccountController accountController = new AccountController(accountService);
        assertEquals(new RegisterResponse(null,null,null), accountController
                .addNewUser(new RegisterRequest("Login", "Password", "Password", "jane.doe@example.org")));
        verify(accountService).addNewUser(Mockito.any());
    }
}

