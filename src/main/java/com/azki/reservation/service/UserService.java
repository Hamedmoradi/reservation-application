package com.azki.reservation.service;

import com.azki.reservation.model.UserRequest;
import com.azki.reservation.model.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    List<UserResponse> getAllUser();

    UserResponse getUser(Long id);
}
