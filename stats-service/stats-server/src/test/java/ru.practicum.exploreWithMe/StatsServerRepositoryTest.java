package ru.practicum.exploreWithMe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.exploreWithMe.model.EndpointHit;
import ru.practicum.exploreWithMe.model.ViewStats;
import ru.practicum.exploreWithMe.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class StatsServerRepositoryTest {
    @Autowired
    private StatsServerRepository statisticRepository;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<ViewStats> viewStatsList;

    @BeforeEach
    void setUp() {
        statisticRepository.save(new EndpointHit(null, "PC", "/event/55", "1", LocalDateTime.now()));
        statisticRepository.save(new EndpointHit(null, "IoS", "/event/55", "2", LocalDateTime.now()));
        statisticRepository.save(new EndpointHit(null, "PC", "/event/55", "3", LocalDateTime.now()));
        statisticRepository.save(new EndpointHit(null, "PC", "/event/56", "4", LocalDateTime.now()));
        statisticRepository.save(new EndpointHit(null, "PC", "/user", "1", LocalDateTime.now()));
        start = LocalDateTime.now().minusDays(1);
        end = LocalDateTime.now().plusDays(1);
    }

    @Test
    void shouldGetStatisticIfUniqueAndWithArray() {
        String[] uris = new String[]{"/event/55", "/event/56"};
        viewStatsList = statisticRepository.getStatisticIfUniqueAndWithArray(start, end, uris);
        assertEquals(3, viewStatsList.size());
        assertEquals("PC", viewStatsList.get(0).getApp());
        assertEquals("/event/55", viewStatsList.get(0).getUri());
        assertEquals(2, viewStatsList.get(0).getHits());
    }

    @Test
    void shouldGetStatisticIfUniqueAndWithoutArray() {
        viewStatsList = statisticRepository.getStatisticIfUniqueAndWithoutArray(start, end);
        assertEquals(4, viewStatsList.size());
        assertEquals("PC", viewStatsList.get(0).getApp());
        assertEquals("/event/55", viewStatsList.get(0).getUri());
        assertEquals(2, viewStatsList.get(0).getHits());
    }
}