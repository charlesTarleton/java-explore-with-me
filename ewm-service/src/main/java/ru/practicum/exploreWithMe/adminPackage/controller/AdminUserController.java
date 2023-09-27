package ru.practicum.exploreWithMe.adminPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.adminPackage.service.userService.AdminUserService;
import ru.practicum.exploreWithMe.commonFiles.user.dto.NewUserRequest;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.USER_CONTROLLER_LOG;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final AdminUserService adminService;

    @GetMapping
    public List<UserDto> getUsersWithSettings(@RequestParam(value = "ids", required = false) Long[] users,
                                              @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                  Integer from,
                                              @Positive @RequestParam(value = "size", defaultValue = "10")
                                                  Integer size) {
        log.info(USER_CONTROLLER_LOG, "получение списка пользователей", "");
        return adminService.getUsersWithSettings(users, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserRequest userDto) {
        log.info(USER_CONTROLLER_LOG, "добавление нового пользователя: ", userDto);
        return adminService.addUser(userDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info(USER_CONTROLLER_LOG, "удаление пользователя с id: ", userId);
        adminService.deleteUser(userId);
    }
}