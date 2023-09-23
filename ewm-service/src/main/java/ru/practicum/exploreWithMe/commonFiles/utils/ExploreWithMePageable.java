package ru.practicum.exploreWithMe.commonFiles.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ExploreWithMePageable extends PageRequest {
    public ExploreWithMePageable(int from, int size, Sort sort) {
        super(from > 0 ? from / size : 0, size, sort);
    }
}
