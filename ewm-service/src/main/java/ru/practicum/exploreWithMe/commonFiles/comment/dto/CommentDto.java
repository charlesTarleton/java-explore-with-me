package ru.practicum.exploreWithMe.commonFiles.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserShortDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    private String text;

    private UserShortDto author;

    private Long eventId;

	private Boolean edited;
}
