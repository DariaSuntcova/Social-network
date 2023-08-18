package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.dto.account.*;
import ru.effectivemobile.dto.notification.NotificationListResponse;
import ru.effectivemobile.entity.Role;
import ru.effectivemobile.entity.User;
import ru.effectivemobile.exceptions.BadRequestException;
import ru.effectivemobile.repository.UserRepository;
import ru.effectivemobile.security.JwtTokenUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils tokenUtils;
    private void checkPassword(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new BadRequestException("Пароли не совпадают");
        }
    }

    public ResponseWithToken addNewUser(RegistrationRequest request) {
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

        return new ResponseWithToken(tokenUtils.generateToken(user));
    }

    public void setPassword(String login, ChangePasswordRequest changePasswordDto) {
        checkPassword(changePasswordDto.password(), changePasswordDto.passwordConfirm());

        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        user.setPassword(changePasswordDto.password());
        userRepository.save(user);
    }



    public void setEmail(String login, ChangeEmailRequest changeEmailDto) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        user.setEmail(changeEmailDto.email());
        userRepository.save(user);
    }

    public NotificationListResponse getNotification(String name) {
        //TODO доделать
        return null;
    }





}
