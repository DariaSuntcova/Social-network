package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.dto.account.ChangeEmailRequest;
import ru.effectivemobile.dto.account.ChangePasswordRequest;
import ru.effectivemobile.dto.account.RegisterRequest;
import ru.effectivemobile.dto.account.RegisterResponse;
import ru.effectivemobile.entity.Role;
import ru.effectivemobile.entity.User;
import ru.effectivemobile.exceptions.BadRequestException;
import ru.effectivemobile.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private void checkPassword(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new BadRequestException("Пароли не совпадают");
        }
    }

    public RegisterResponse addNewUser(RegisterRequest request) {
        if (request.login() == null ||request.login().isEmpty()) {
            throw new BadRequestException("Логин не может быть пустым");
        }
        if (request.password() == null ||request.password().isEmpty()) {
            throw new BadRequestException("Пароль не может быть пустым");
        }
        if (request.email() == null ||request.email().isEmpty()) {
            throw new BadRequestException("Емейл не может быть пустым");
        }

        checkPassword(request.password(), request.passwordConfirm());

        Optional<User> userOptional = userRepository.findByLogin(request.login());
        if (userOptional.isPresent()) {
            throw new BadRequestException("Пользователь с таким логином уже существует");
        }

        userOptional = userRepository.findByEmail(request.login());
        if (userOptional.isPresent()) {
            throw new BadRequestException("Пользователь с таким email уже существует");
        }

        User user = User.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .role(Role.USER)
        .build();

        user = userRepository.save(user);

        return new RegisterResponse(user.getId(), user.getLogin(), user.getEmail());
    }

    public void setPassword(String login, ChangePasswordRequest changePasswordDto) {
        if (changePasswordDto.password() == null ||changePasswordDto.password().isEmpty()) {
            throw new BadRequestException("Пароль не может быть пустым");
        }
        checkPassword(changePasswordDto.password(), changePasswordDto.passwordConfirm());

        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        user.setPassword(passwordEncoder.encode(changePasswordDto.password()));
        userRepository.save(user);
    }



    public void setEmail(String login, ChangeEmailRequest changeEmailDto) {
        if (changeEmailDto.email() == null ||changeEmailDto.email().isEmpty()) {
            throw new BadRequestException("Емейл не может быть пустым");
        }

        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        user.setEmail(changeEmailDto.email());
        userRepository.save(user);
    }
}

