package com.azki.reservation.service;

import com.azki.reservation.entity.User;
import com.azki.reservation.filter.JwtFilter;
import com.azki.reservation.model.AuthRequest;
import com.azki.reservation.repository.UserRepository;
import com.azki.reservation.service.impl.AuthServiceImpl;
import com.azki.reservation.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_success() {
        // given
        AuthRequest request = new AuthRequest("hamed", "password_789");

        User user = new User();
        user.setUsername("hamed");
        user.setPassword("password_789");

        when(userRepository.findByUsername("hamed"))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password_789", "password_789"))
            .thenReturn(true);

        when(jwtUtil.generateToken("hamed"))
            .thenReturn("jwt-token");

        // when
        String token = authService.login(request);

        // then
        assertEquals("jwt-token", token);

        verify(userRepository).findByUsername("hamed");
        verify(passwordEncoder).matches("password_789", "password_789");
        verify(jwtUtil).generateToken("hamed");
    }

    @Test
    void login_userNotFound() {
        // given
        AuthRequest request = new AuthRequest("hamed", "password_789");

        when(userRepository.findByUsername("hamed"))
            .thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(request));

        assertEquals("user not found.", exception.getMessage());
    }

    @Test
    void login_invalidPassword() {
        // given
        AuthRequest request = new AuthRequest("hamed", "wrong");

        User user = new User();
        user.setUsername("hamed");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("hamed"))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encodedPassword"))
            .thenReturn(false);

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(request));

        assertEquals("invalid credentials.", exception.getMessage());

        verify(jwtUtil, never()).generateToken(anyString());
    }
}
