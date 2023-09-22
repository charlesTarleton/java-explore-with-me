package ru.practicum.exploreWithMe.adminPackage.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.adminPackage.service.userService.AdminUserServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdminUserControllerTest {
    @Mock
    private AdminUserServiceImpl userService;

    @InjectMocks
    private AdminUserController userController;

    @Test
    void shouldGetUsersWithSettings() throws Exception {
        String ids = Arrays.stream(new Long[]{2L})
                .map(Object::toString)
                .collect(Collectors.joining(","));
        MockMvc mvc = MockMvcBuilders.standaloneSetup(userController).build();

        when(userService.getUsersWithSettings(any(), anyInt(), anyInt()))
                .thenReturn(List.of(new UserDto(2L, "Имя", "email2@mail.ru")));

        mvc.perform(get("/admin/users")
                        .param("ids", ids)
                        .param("from", "1")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Имя")))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder("email2@mail.ru")));
    }
}
