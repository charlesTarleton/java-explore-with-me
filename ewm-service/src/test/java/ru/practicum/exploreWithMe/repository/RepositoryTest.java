package ru.practicum.exploreWithMe.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.commonFiles.comment.model.Comment;
import ru.practicum.exploreWithMe.commonFiles.comment.repository.CommentRepository;
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

    @Autowired
    private CommentRepository commentRepository;

    private User initiator;
    private Category category;
    private Location location;
    private Event event;

    @BeforeEach
    void setUp() {
        initiator = new User(1L, "Имя", "email1@mail.ru");
        category = categoryRepository.save(new Category(1L, "Категория"));
        location = locationRepository.save(new Location(1L, 10.4f, 11.6f));
        event = new Event(1L,
                "Аннотация", category, 2L, LocalDateTime.now().minusDays(1),
                "Описание", LocalDateTime.now().plusDays(2), initiator, location, true,
                10L, LocalDateTime.now(), true, EventState.PUBLISHED,
                "Заголовок события", 11);

        userRepository.save(initiator);
        event = eventRepository.save(event);
        compilationRepository.save(new Compilation(1L, true, "Подборка", Set.of(event)));
    }

    @Test
    void shouldGetCompilations() {
        List<Compilation> compilationList = compilationRepository
                .getCompilations(true, new ExploreWithMePageable(0, 2, Sort.unsorted()));
        assertEquals(1, compilationList.size());
        assertEquals(1L, compilationList.get(0).getId());
        assertEquals(true, compilationList.get(0).getPinned());
        assertEquals("Подборка", compilationList.get(0).getTitle());
        assertEquals("Аннотация", new ArrayList<>(compilationList.get(0).getEvents()).get(0).getAnnotation());
    }

    @Test
    void shouldGetEventsComments() {
        User author1 = userRepository.save(new User(2L, "Имя2", "email2@mail.ru"));
        User author2 = userRepository.save(new User(3L, "Имя3", "email3@mail.ru"));
        Event event2 = new Event(2L,
                "Аннотация", category, 2L, LocalDateTime.now().minusDays(1),
                "Описание", LocalDateTime.now().plusDays(2), initiator, location, true,
                10L, LocalDateTime.now(), true, EventState.PUBLISHED,
                "Заголовок события", 11);
        eventRepository.save(event2);
        commentRepository.save(new Comment(null, "Комментарий1",
                author1, event, LocalDateTime.now(), false));
        commentRepository.save(new Comment(null, "Комментарий2",
                author2, event2, LocalDateTime.now(), false));

        List<Comment> commentList = commentRepository.getEventsComments(List.of(2L));
        assertEquals(1, commentList.size());
        assertEquals(2L, commentList.get(0).getId());
        assertEquals("Комментарий2", commentList.get(0).getText());
        assertEquals(3L, commentList.get(0).getAuthor().getId());
        assertEquals(2L, commentList.get(0).getEvent().getId());
        assertEquals(false, commentList.get(0).getEdited());
    }
}
