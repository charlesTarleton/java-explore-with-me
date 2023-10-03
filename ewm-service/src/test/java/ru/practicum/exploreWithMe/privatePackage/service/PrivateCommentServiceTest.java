package ru.practicum.exploreWithMe.privatePackage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.NewCommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.model.Comment;
import ru.practicum.exploreWithMe.commonFiles.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventNotPublicisedException;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;
import ru.practicum.exploreWithMe.privatePackage.service.commentService.PrivateCommentServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrivateCommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PrivateCommentServiceImpl commentService;

    private User author;
    private Event event;

    @BeforeEach
    void setUp() {
        User initiator = new User(3L, "Имя", "email1@mail.ru");
        author = new User(10L, "Имя2", "email2@mail.ru");
        event = new Event(2L, "Аннотация", new Category(5L, "Категория"),
                2L, LocalDateTime.now().minusDays(1), "Описание",
                LocalDateTime.now().plusDays(1), initiator, new Location(6L, 10.5f, 40.11f),
                true, 10L, LocalDateTime.now(), true,
                EventState.PUBLISHED, "Заголовок", 100);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
    }

    @Test
    void shouldAddComment() {
        when(commentRepository.save(any(Comment.class)))
                .thenReturn((new Comment(1L, "Комментарий", author, event, LocalDateTime.now(), false)));

        CommentDto comment = commentService.addComment(10L, new NewCommentDto("Комментарий", 2L));
        assertEquals(1L, comment.getId());
        assertEquals("Комментарий", comment.getText());
        assertEquals(10L, comment.getAuthor().getId());
        assertEquals(2L, comment.getEventId());
        assertEquals(false, comment.getEdited());
    }

    @Test
    void shouldNotAddComment() {
        event.setStateAction(EventState.PENDING);

        assertThrows(EventNotPublicisedException.class,
                () -> commentService.addComment(10L, new NewCommentDto("Комментарий", 2L)));
    }
}
