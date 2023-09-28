package ru.practicum.exploreWithMe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMe.model.ViewStats;
import ru.practicum.exploreWithMe.repository.StatsServerRepository;
import ru.practicum.exploreWithMe.service.StatsServerServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatsServerServiceTest {
    @Mock
    private StatsServerRepository statisticRepository;
    @InjectMocks
    private StatsServerServiceImpl statisticService;

    @Test
    void shouldGetStatistic() {
        LocalDateTime start = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);;
        when(statisticRepository.getStatisticIfNotUniqueAndWithoutArray(start, end))
                .thenReturn(List.of(new ViewStats("app", "uri", 2L)));
        List<ViewStatsDto> viewStatsDtoList = statisticService.getStatistic(start, end, new String[]{}, false);
        assertNotNull(viewStatsDtoList);
        assertEquals(1, viewStatsDtoList.size());
        assertEquals("app", viewStatsDtoList.get(0).getApp());
        assertEquals("uri", viewStatsDtoList.get(0).getUri());
        assertEquals(2L, viewStatsDtoList.get(0).getHits());
        verify(statisticRepository, times(1))
                .getStatisticIfNotUniqueAndWithoutArray(any(), any());
    }
}