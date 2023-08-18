package ru.effectivemobile.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.effectivemobile.dto.account.RegistrationRequest;
import ru.effectivemobile.dto.account.ResponseWithToken;
import ru.effectivemobile.entity.User;
import ru.effectivemobile.exceptions.BadRequestException;
import ru.effectivemobile.repository.UserRepository;
import ru.effectivemobile.security.JwtTokenUtils;

@ContextConfiguration(classes = {AccountService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
class AccountServiceTest {
    private static final String LOGIN = "testLogin";
    private static final String PASSWORD = "testPassword";
    private static final String EMAIL = "test@mail.ru";
    private static final String TOKEN = "token";

    @Autowired
    private AccountService accountService;
    @MockBean
    private JwtTokenUtils jwtTokenUtils;
    @MockBean
    private UserRepository userRepository;

    @Test
    void testAddNewUserBadPasswordConfirm() {
        assertThrows(BadRequestException.class, () -> accountService
                .addNewUser(new RegistrationRequest(LOGIN, PASSWORD, "Password Confirm", EMAIL)));
    }

    @Test
    void testAddNewUserExistsLogin() {
        when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.of(new User()));
        assertThrows(BadRequestException.class, () -> accountService.addNewUser(
                new RegistrationRequest(LOGIN, PASSWORD, PASSWORD, EMAIL)));
        verify(userRepository).findByLogin(Mockito.any());
    }

    @Test
    void testAddNewUserExistsEmail() {
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(new User()));
        when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> accountService.addNewUser(
                new RegistrationRequest(LOGIN, PASSWORD, PASSWORD, EMAIL)));
        verify(userRepository).findByEmail(Mockito.any());
        verify(userRepository).findByLogin(Mockito.any());
    }

    @Test

    void testAddNewUserValidData() {
        when(userRepository.save(Mockito.any())).thenReturn(new User());
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(Mockito.any())).thenReturn(Optional.empty());
        User user = new User();
        when(jwtTokenUtils.generateToken(user)).thenReturn(TOKEN);

        ResponseWithToken response = accountService.addNewUser(new RegistrationRequest(LOGIN, PASSWORD, PASSWORD, EMAIL));

        assertEquals(response.authToken(), TOKEN);
        verify(userRepository).findByEmail(Mockito.any());
        verify(userRepository).findByLogin(Mockito.any());
        verify(userRepository).save(Mockito.any());
        verify(jwtTokenUtils).generateToken(Mockito.any());
    }

}

