package com.azki.reservation.controller;

import com.azki.reservation.filter.JwtFilter;
import com.azki.reservation.model.UserRequest;
import com.azki.reservation.model.UserResponse;
import com.azki.reservation.service.UserService;
import com.azki.reservation.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;


    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void registerUser_success() throws Exception {
        UserRequest request = new UserRequest("hamed", "hamed@test.com", "password_789");

        UserResponse response = new UserResponse(
            1L,
            "hamed",
            "hamed@test.com",
            "$2a$10$QcIhZNJ6iKtTNoQu6IY61O45zvt6Z2opHUODJYexe5dH52gjwbEue",
            LocalDateTime.now()
        );

        when(userService.createUser(any(UserRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value("hamed"))
            .andExpect(jsonPath("$.email").value("hamed@test.com"));
    }

    @Test
    void getUser_success() throws Exception {
        UserResponse response = new UserResponse(
            1L,
            "hamed",
            "hamed@test.com",
            "password",
            LocalDateTime.now()
        );

        when(userService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/users/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value("hamed"))
            .andExpect(jsonPath("$.email").value("hamed@test.com"));
    }

    @Test
    void getAllUsers_success() throws Exception {
        List<UserResponse> users = List.of(
            new UserResponse(1L, "hamed", "hamed@test.com", "pass", LocalDateTime.now()),
            new UserResponse(2L, "mary", "mary@test.com", "pass", LocalDateTime.now())
        );

        when(userService.getAllUser()).thenReturn(users);

        mockMvc.perform(get("/users/all-users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].username").value("hamed"))
            .andExpect(jsonPath("$[1].username").value("mary"));
    }
}
