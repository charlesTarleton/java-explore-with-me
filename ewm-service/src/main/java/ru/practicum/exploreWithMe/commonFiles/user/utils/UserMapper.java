package ru.practicum.exploreWithMe.commonFiles.user.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.commonFiles.user.dto.NewUserRequest;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserDto;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserShortDto;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;

@UtilityClass
@Slf4j
public class UserMapper {
    public UserShortDto toShortDto(User user) {
        log.info("Начата процедура преобразования пользователя в сокращенное ДТО: {}", user);
        return new UserShortDto(user.getId(), user.getName());
    }

    public UserDto toDto(User user) {
        log.info("Начата процедура преобразования пользователя в ДТО: {}", user);
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toUser(NewUserRequest userDto) {
        log.info("Начата процедура преобразования нового ДТО в пользователя {}: ", userDto);
        return new User(null, userDto.getName(), userDto.getEmail());
    }
}
