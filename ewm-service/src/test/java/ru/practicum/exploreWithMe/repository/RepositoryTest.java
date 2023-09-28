package ru.practicum.exploreWithMe.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.commonFiles.compilation.model.Compilation;
import ru.practicum.exploreWithMe.commonFiles.compilation.repository.CompilationRepository;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.repository.LocationRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class RepositoryTest {
    @Autowired
    private CompilationRepository compilationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldGetCompilations() {
        User user = new User(1L, "Имя", "email1@mail.ru");
        Category category = categoryRepository.save(new Category(1L, "Категория"));
        Location location = locationRepository.save(new Location(1L, 10.4f, 11.6f));
        Event event = new Event(1L,
                "Аннотация", category, 2L, LocalDateTime.now().minusDays(1),
                "Описание", LocalDateTime.now().plusDays(2), user, location, true,
                10L, LocalDateTime.now(), true, EventState.PUBLISHED,
                "Заголовок события", 11);
        userRepository.save(user);
        eventRepository.save(event);
        compilationRepository.save(new Compilation(1L, true, "Подборка", Set.of(event)));
        List<Compilation> compilationList = compilationRepository
                .getCompilations(true, new ExploreWithMePageable(0, 2, Sort.unsorted()));
        assertEquals(1, compilationList.size());
        assertEquals(1L, compilationList.get(0).getId());
        assertEquals(true, compilationList.get(0).getPinned());
        assertEquals("Подборка", compilationList.get(0).getTitle());
        assertEquals("Аннотация", new ArrayList<>(compilationList.get(0).getEvents()).get(0).getAnnotation());
    }

}
