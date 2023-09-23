package ru.practicum.exploreWithMe;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.dto.EndpointHitDto;
import ru.practicum.exploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMe.service.StatsServerServiceImpl;
import ru.practicum.exploreWithMe.utils.AppDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatsServerIntegrationTest {
    private final StatsServerServiceImpl statisticService;

    @Test
    void shouldGetOwnerBookingsIntegration() {
        statisticService.saveStatistic(new EndpointHitDto("uri1", "ip",
                LocalDateTime.now().minusDays(1)));
        statisticService.saveStatistic(new EndpointHitDto("uri2", "ip",
                LocalDateTime.now().minusDays(2)));
        statisticService.saveStatistic(new EndpointHitDto("uri1", "ip",
                LocalDateTime.now().plusDays(1)));
        List<ViewStatsDto> viewStatsDtoList = statisticService.getStatistic(
                AppDateTimeFormatter.toString(LocalDateTime.now().minusDays(4)),
                AppDateTimeFormatter.toString(LocalDateTime.now().plusDays(4)),
                new String[]{"[uri1]", "[uri2]"}, false);
        assertEquals(2, viewStatsDtoList.size());
        assertEquals(2, viewStatsDtoList.get(0).getHits());
        assertEquals(1, viewStatsDtoList.get(1).getHits());
        assertEquals("uri1", viewStatsDtoList.get(0).getUri());
        assertEquals("uri2", viewStatsDtoList.get(1).getUri());
    }
}
