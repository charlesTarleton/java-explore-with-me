package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
