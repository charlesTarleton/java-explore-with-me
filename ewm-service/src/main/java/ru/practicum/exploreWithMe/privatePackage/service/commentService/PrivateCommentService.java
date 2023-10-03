package ru.practicum.exploreWithMe.privatePackage.service.commentService;

import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.NewCommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.UpdateCommentDto;

import java.util.List;

public interface PrivateCommentService {
    List<CommentDto> getUserComments(Long userId, Integer from, Integer size);

    CommentDto addComment(Long userId, NewCommentDto commentDto);

    CommentDto getComment(Long userId, Long commentId);

    CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto commentDto);

    void deleteComment(Long userId, Long commentId);
}
