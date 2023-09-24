package ru.practicum.exploreWithMe.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.adminPackage.service.categoryService.AdminCategoryServiceImpl;
import ru.practicum.exploreWithMe.adminPackage.service.eventService.AdminEventServiceImpl;
import ru.practicum.exploreWithMe.adminPackage.service.userService.AdminUserServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.category.dto.NewCategoryDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.NewEventDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventAdminRequest;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.utils.AdminAction;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.user.dto.NewUserRequest;
import ru.practicum.exploreWithMe.privatePackage.service.eventService.PrivateEventServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminEventIntegrationTest {
    private final AdminUserServiceImpl userService;
    private final PrivateEventServiceImpl privateEventService;
    private final AdminCategoryServiceImpl categoryService;
    private final AdminEventServiceImpl adminEventService;

    @Test
    void shouldUpdateEvent() {
        userService.addUser(new NewUserRequest("Имя", "email1@mail.ru"));
        categoryService.addCategory(new NewCategoryDto("Категория"));

        privateEventService.addEvent(1L, new NewEventDto("Аннотация", 1L,
                "Описание", LocalDateTime.now().plusDays(2), new Location(null, 10.4f, 11.6f),
                true, 10L, true, "Заголовок события"));
        EventFullDto updateEvent = adminEventService.updateEvent(1L,
                new UpdateEventAdminRequest("Аннотация", 1L, "Описание",
                        LocalDateTime.now().plusDays(2), new Location(null, 10.4f, 11.6f),
                true, 10L, true, AdminAction.PUBLISH_EVENT, "Заголовок события"));

        assertEquals(1L, updateEvent.getId());
        assertEquals("Аннотация", updateEvent.getAnnotation());
        assertEquals(EventState.PUBLISHED, updateEvent.getState());
    }
}
