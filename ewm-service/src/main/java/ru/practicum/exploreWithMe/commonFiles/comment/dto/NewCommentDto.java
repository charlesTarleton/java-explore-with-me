package ru.practicum.exploreWithMe.commonFiles.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank
    private String text;

    @NotNull
    private Long eventId;
}
