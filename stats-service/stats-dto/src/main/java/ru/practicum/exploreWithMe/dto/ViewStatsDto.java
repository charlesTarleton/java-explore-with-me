package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ViewStatsDto {
    private String app;
    private String uri;
    private int hits;
}
