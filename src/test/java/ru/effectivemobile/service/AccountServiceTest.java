package ru.effectivemobile.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.effectivemobile.dto.account.RegisterRequest;
import ru.effectivemobile.dto.account.RegisterResponse;
import ru.effectivemobile.entity.User;
import ru.effectivemobile.exceptions.BadRequestException;
import ru.effectivemobile.repository.UserRepository;
import ru.effectivemobile.security.JwtTokenUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AccountService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
class AccountServiceTest {
    private static final String LOGIN = "testLogin";
    private static final String PASSWORD = "testPassword";
    private static final String EMAIL = "test@mail.ru";

    @Autowired
    private AccountService accountService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void testAddNewUserBadPasswordConfirm() {
        assertThrows(BadRequestException.class, () -> accountService
                .addNewUser(new RegisterRequest(LOGIN, PASSWORD, "Password Confirm", EMAIL)));
    }

    @Test
    void testAddNewUserExistsLogin() {
        when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.of(new User()));
        assertThrows(BadRequestException.class, () -> accountService.addNewUser(
                new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL)));
        verify(userRepository).findByLogin(Mockito.any());
    }

    @Test
    void testAddNewUserExistsEmail() {
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(new User()));
        when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> accountService.addNewUser(
                new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL)));
        verify(userRepository).findByEmail(Mockito.any());
        verify(userRepository).findByLogin(Mockito.any());
    }

    @Test

    void testAddNewUserValidData() {
        when(userRepository.save(Mockito.any())).thenReturn(new User());
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.empty());

        RegisterResponse response = accountService.addNewUser(new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL));

        verify(userRepository).findByEmail(Mockito.any());
        verify(userRepository).findByLogin(Mockito.any());
        verify(userRepository).save(Mockito.any());

    }

}

