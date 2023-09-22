package ru.practicum.exploreWithMe.adminPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.adminPackage.service.userService.AdminUserService;
import ru.practicum.exploreWithMe.commonFiles.user.dto.NewUserRequest;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminService;
    private static final String CONTROLLER_LOG = "Контроллер пользователей администратора получил запрос на {}{}";

    @GetMapping
    public List<UserDto> getUsersWithSettings(@RequestParam(value = "ids", required = false) Long[] users,
                                              @RequestParam(value = "from", defaultValue = "0") Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info(CONTROLLER_LOG, "получение списка пользователей", "");
        return adminService.getUsersWithSettings(users, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserRequest userDto) {
        log.info(CONTROLLER_LOG, "добавление нового пользователя: ", userDto);
        return adminService.addUser(userDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info(CONTROLLER_LOG, "удаление пользователя с id: ", userId);
        adminService.deleteUser(userId);
    }
}