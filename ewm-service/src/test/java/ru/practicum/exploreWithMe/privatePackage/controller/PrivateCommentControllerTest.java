package ru.practicum.exploreWithMe.privatePackage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.NewCommentDto;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserShortDto;
import ru.practicum.exploreWithMe.privatePackage.service.commentService.PrivateCommentServiceImpl;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PrivateCommentControllerTest {
    @Mock
    private PrivateCommentServiceImpl commentService;

    @InjectMocks
    private PrivateCommentController commentController;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @Test
    void shouldAddComment() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(commentController).build();

        UserShortDto user = new UserShortDto(4L, "Имя");
        when(commentService.addComment(anyLong(), any()))
                .thenReturn(new CommentDto(1L, "Комментарий", user, 3L, false));

        mvc.perform(post("/users/4/comments")
                .content(mapper.writeValueAsString(new NewCommentDto("Комментарий", 3L)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Комментарий")))
                .andExpect(jsonPath("$.author", is(notNullValue())))
                .andExpect(jsonPath("$.eventId", is(3)))
                .andExpect(jsonPath("$.edited", is(false)));
    }
}
