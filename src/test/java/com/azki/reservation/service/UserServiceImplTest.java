package com.azki.reservation.service;

import com.azki.reservation.entity.BaseEntity;
import com.azki.reservation.entity.User;
import com.azki.reservation.exception.UserNotFoundException;
import com.azki.reservation.mapper.UserMapper;
import com.azki.reservation.model.UserRequest;
import com.azki.reservation.model.UserResponse;
import com.azki.reservation.repository.UserRepository;
import com.azki.reservation.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success() {
        // given
        UserRequest request = new UserRequest("hamed", "hamed@test.com", "H@mor1991");

        User user = new User();
        user.setUsername("hamed");
        user.setEmail("hamed@test.com");
        user.setPassword("H@mor1991");

        UserResponse response = new UserResponse(
                1L,
                "hamed",
                "hamed@test.com",
                "encodedPass",
                LocalDateTime.now()
        );

        when(userMapper.usercreateRequestToUser(request)).thenReturn(user);
        when(passwordEncoder.encode("H@mor1991")).thenReturn("encodedPass");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserCreateResponse(user)).thenReturn(response);

        // when
        UserResponse result = userService.createUser(request);

        // then
        assertNotNull(result);
        assertEquals("hamed", result.username());
        assertEquals("hamed@test.com", result.email());
        assertEquals("encodedPass", result.password());

        verify(userMapper).usercreateRequestToUser(request);
        verify(passwordEncoder).encode("H@mor1991");
        verify(userRepository).save(user);
        verify(userMapper).userToUserCreateResponse(user);
    }

    @Test
    void getAllUser_success() {
        // given
        User user1 = new User("hamed", "hamed@test.com", "H@mor1369");
        User user2 = new User("mary", "mary@test.com", "M@ry234");

        List<User> users = List.of(user1, user2);

        UserResponse r1 = new UserResponse(1L, "hamed", "hamed@test.com", "H@mor1369", LocalDateTime.now());
        UserResponse r2 = new UserResponse(2L, "mary", "mary@test.com", "M@ry234", LocalDateTime.now());

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userListToUserCreateResponseList(users))
                .thenReturn(List.of(r1, r2));

        // when
        List<UserResponse> result = userService.getAllUser();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(userRepository).findAll();
        verify(userMapper).userListToUserCreateResponseList(users);
    }

    @Test
    void getUser_success() {
        // given
        User user = new User("hamed", "hamed@test.com", "H@mor1369");
        setId(user, 1L);

        UserResponse response =
                new UserResponse(1L, "hamed", "hamed@test.com", "H@mor1369", LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserCreateResponse(user)).thenReturn(response);

        // when
        UserResponse result = userService.getUser(1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("hamed", result.username());

        verify(userRepository).findById(1L);
        verify(userMapper).userToUserCreateResponse(user);
    }

    @Test
    void getUser_notFound_shouldThrowException() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(1L));

        verify(userRepository).findById(1L);
        verifyNoInteractions(userMapper);
    }

    private void setId(BaseEntity entity, Long id) {
        try {
            var field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
