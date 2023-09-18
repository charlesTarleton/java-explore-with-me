package ru.practicum.exploreWithMe.adminPackage.service.userService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.UserExistException;
import ru.practicum.exploreWithMe.commonFiles.user.dto.NewUserRequest;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserDto;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;
import ru.practicum.exploreWithMe.commonFiles.user.utils.UserMapper;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private static final String SERVICE_LOG = "Сервис пользователей администратора получил запрос на {}{}";

    @Transactional(readOnly = true)
    public List<UserDto> getUsersWithSettings(Long[] users, Integer from, Integer size) {
        log.info(SERVICE_LOG, "получение списка пользователей", "");
        return userRepository.getUsersByIds(users, new ExploreWithMePageable(from, size, Sort.unsorted())).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto addUser(NewUserRequest userDto) {
        log.info(SERVICE_LOG, "добавление нового пользователя: ", userDto);
        return UserMapper.toDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public void deleteUser(Long userId) {
        log.info(SERVICE_LOG, "удаление пользователя с id: ", userId);
        checkUserIsExist(userId);
        userRepository.deleteById(userId);
    }

    private void checkUserIsExist(Long userId) {
        log.info("Начата процедура проверки наличия пользователя с id: {}", userId);
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserExistException("Указанный пользователь не найден");
        }
    }
}
