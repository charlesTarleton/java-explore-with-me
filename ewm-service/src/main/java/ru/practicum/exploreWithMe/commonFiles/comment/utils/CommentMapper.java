package ru.practicum.exploreWithMe.commonFiles.comment.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.NewCommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.UpdateCommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.model.Comment;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.utils.UserMapper;

@UtilityClass
@Slf4j
public class CommentMapper {
    public Comment toComment(NewCommentDto commentDto, User user, Event event) {
        log.info("Начата процедура преобразования нового ДТО в комментарий: {}", commentDto);
        return new Comment(null, commentDto.getText(), user, event, null, false);
    }

	public Comment toComment(UpdateCommentDto commentDto, Comment oldComment) {
        log.info("Начата процедура преобразования измененного ДТО в комментарий: {}", commentDto);
        return new Comment(oldComment.getId(), commentDto.getText(), oldComment.getAuthor(),
                oldComment.getEvent(), oldComment.getCreatedOn(), true);
    }

    public CommentDto toDto(Comment comment) {
        log.info("Начата процедура преобразования комментария в ДТО: {}", comment);
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                UserMapper.toShortDto(comment.getAuthor()),
                comment.getEvent().getId(),
				comment.getEdited());
    }
}
