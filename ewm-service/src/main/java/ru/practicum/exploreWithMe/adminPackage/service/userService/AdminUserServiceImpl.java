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
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.USER_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDto> getUsersWithSettings(Long[] users, Integer from, Integer size) {
        log.info(USER_SERVICE_LOG, "получение списка пользователей", "");
        Set<Long> usersSet = null;
        if (users != null) {
            usersSet = Set.of(users);
        }
        return userRepository.getUsersByIds(usersSet, new ExploreWithMePageable(from, size, Sort.unsorted())).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto addUser(NewUserRequest userDto) {
        log.info(USER_SERVICE_LOG, "добавление нового пользователя: ", userDto);
        return UserMapper.toDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public void deleteUser(Long userId) {
        log.info(USER_SERVICE_LOG, "удаление пользователя с id: ", userId);
        checkUserIsExist(userId);
        userRepository.deleteById(userId);
    }

    private void checkUserIsExist(Long userId) {
        log.info("Начата процедура проверки наличия пользователя с id: {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new UserExistException("Указанный пользователь не найден"));
    }
}
