package com.azki.reservation.service.impl;

import com.azki.reservation.entity.User;
import com.azki.reservation.enums.AppExceptionStatusEnum;
import com.azki.reservation.exception.InvalidCredentialsException;
import com.azki.reservation.exception.UserNotFoundException;
import com.azki.reservation.model.AuthRequest;
import com.azki.reservation.repository.UserRepository;
import com.azki.reservation.service.AuthService;
import com.azki.reservation.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Override
    public String login(AuthRequest request) {
        User user = userRepository.findByUsername(request.username()).orElseThrow(
                () -> new UserNotFoundException(AppExceptionStatusEnum.USER_NOT_FOUND.getStatus(), AppExceptionStatusEnum.USER_NOT_FOUND.getMessage()));

        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername());
        } else {
            throw new InvalidCredentialsException(AppExceptionStatusEnum.INVALID_CREDENTIALS.getStatus(), AppExceptionStatusEnum.INVALID_CREDENTIALS.getMessage());
        }
    }
}
