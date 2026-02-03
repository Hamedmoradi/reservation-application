package com.azki.reservation.service.impl;

import com.azki.reservation.entity.User;
import com.azki.reservation.enums.AppExceptionStatusEnum;
import com.azki.reservation.exception.UserNotFoundException;
import com.azki.reservation.mapper.UserMapper;
import com.azki.reservation.model.UserRequest;
import com.azki.reservation.model.UserResponse;
import com.azki.reservation.repository.UserRepository;
import com.azki.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.usercreateRequestToUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.userToUserCreateResponse(user);
    }

    @Override
    public List<UserResponse> getAllUser() {
        return userMapper.userListToUserCreateResponseList(userRepository.findAll());
    }

    @Override
    public UserResponse getUser(Long id) {
        return userMapper.userToUserCreateResponse(userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException(AppExceptionStatusEnum.USER_NOT_FOUND.getStatus(), AppExceptionStatusEnum.USER_NOT_FOUND.getMessage())));
    }
}
