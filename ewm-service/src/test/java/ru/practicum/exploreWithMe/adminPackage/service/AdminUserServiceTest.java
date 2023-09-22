package ru.practicum.exploreWithMe.adminPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.adminPackage.service.userService.AdminUserServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserServiceImpl userService;

    @Test
    void should() {

    }
}
