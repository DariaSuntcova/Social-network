package ru.effectivemobile.service;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.effectivemobile.dto.account.ResponseWithToken;
import ru.effectivemobile.dto.auth.AuthRequest;
import ru.effectivemobile.exceptions.AuthenticationException;
import ru.effectivemobile.security.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private static final Logger log = Logger.getLogger(AuthService.class);

    public ResponseWithToken createAuthToken(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password()));
        } catch (BadCredentialsException e) {
            log.error("Wrong password");
            throw new AuthenticationException("Bad credentials");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.login());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new ResponseWithToken(token);
    }
}
