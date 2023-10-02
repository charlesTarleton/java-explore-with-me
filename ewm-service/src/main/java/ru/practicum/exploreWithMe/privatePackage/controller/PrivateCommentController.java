package ru.practicum.exploreWithMe.privatePackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.NewCommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.UpdateCommentDto;
import ru.practicum.exploreWithMe.privatePackage.service.commentService.PrivateCommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Private.COMMENT_CONTROLLER_LOG;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class PrivateCommentController {
    private final PrivateCommentService commentService;

    @GetMapping
    public List<CommentDto> getUserComments(@PathVariable("userId") Long userId,
                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                            Integer from,
                                            @Positive @RequestParam(value = "size", defaultValue = "10")
                                                Integer size) {
        log.info(COMMENT_CONTROLLER_LOG, "получение всех комментариев пользователя с id: ", userId);
        return commentService.getUserComments(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable("userId") Long userId,
                                 @Valid @RequestBody NewCommentDto commentDto) {
        log.info(COMMENT_CONTROLLER_LOG, "добавление комментария ", commentDto);
        return commentService.addComment(userId, commentDto);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable("userId") Long userId,
                                 @PathVariable("commentId") Long commentId) {
        log.info(COMMENT_CONTROLLER_LOG, "получение комментария по id: ", commentId);
        return commentService.getComment(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable("userId") Long userId,
                                    @PathVariable("commentId") Long commentId,
                                    @Valid @RequestBody UpdateCommentDto commentDto) {
        log.info(COMMENT_CONTROLLER_LOG, "обновление комментария с id: ", commentId);
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("userId") Long userId,
                              @PathVariable("commentId") Long commentId) {
        log.info(COMMENT_CONTROLLER_LOG, "удаление комментария с id: ", commentId);
		commentService.deleteComment(userId, commentId);
    }
}
