package com.azki.reservation.mapper;

import com.azki.reservation.entity.User;
import com.azki.reservation.model.UserRequest;
import com.azki.reservation.model.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User usercreateRequestToUser(UserRequest userRequest);

    UserResponse userToUserCreateResponse(User user);

    List<UserResponse> userListToUserCreateResponseList(List<User> user);
}
