package ru.practicum.exploreWithMe.adminPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.exploreWithMe.adminPackage.service.userService.AdminUserServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserDto;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserServiceImpl userService;

    @Test
    void shouldGetUsersWithSettings() {
        when(userRepository.getUsersByIds(any(), any(Pageable.class)))
                .thenReturn(List.of(new User(2L, "Имя", "email2@mail.ru")));
        List<UserDto> userDtoList = userService.getUsersWithSettings(new Long[]{2L}, 0, 10);
        assertEquals(1, userDtoList.size());
        assertEquals(2L, userDtoList.get(0).getId());
        assertEquals("Имя", userDtoList.get(0).getName());
        assertEquals("email2@mail.ru", userDtoList.get(0).getEmail());
    }
}
