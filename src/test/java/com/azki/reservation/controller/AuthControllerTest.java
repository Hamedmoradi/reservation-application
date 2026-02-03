package com.azki.reservation.controller;

import com.azki.reservation.enums.AppExceptionStatusEnum;
import com.azki.reservation.exception.InvalidCredentialsException;
import com.azki.reservation.model.AuthRequest;
import com.azki.reservation.service.AuthService;
import com.azki.reservation.util.JwtUtil;
import com.azki.reservation.filter.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void login_success() throws Exception {
        AuthRequest request = new AuthRequest("hamed", "H@mor1991");
        String token = "jwt-token";

        Mockito.when(authService.login(any(AuthRequest.class)))
                .thenReturn(token);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    void login_invalidCredentials() throws Exception {
        AuthRequest request = new AuthRequest("hamed", "H@mor19913r4");

        Mockito.when(authService.login(any(AuthRequest.class)))
                .thenThrow(new InvalidCredentialsException(
                        AppExceptionStatusEnum.INVALID_CREDENTIALS.getStatus(),
                        AppExceptionStatusEnum.INVALID_CREDENTIALS.getMessage()
                ));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
