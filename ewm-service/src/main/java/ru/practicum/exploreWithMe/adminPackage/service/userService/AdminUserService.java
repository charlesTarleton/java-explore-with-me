package ru.practicum.exploreWithMe.adminPackage.service.userService;

import ru.practicum.exploreWithMe.commonFiles.user.dto.NewUserRequest;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    public List<UserDto> getUsersWithSettings(Long[] users, Integer from, Integer size);

    public UserDto addUser(NewUserRequest userDto);

    public void deleteUser(Long userId);
}
