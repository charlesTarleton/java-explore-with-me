package ru.practicum.exploreWithMe.commonFiles.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.initiator.id = :userId")
    List<Event> getUserEvents(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.stateAction IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> getEventsWithSettings(@Param("users") Long[] users,
                                      @Param("states") String[] states,
                                      @Param("categories") Long[] categories,
                                      @Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE (:text IS NULL OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "   OR (LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = paid) " +
            "AND (e.eventDate >= COALESCE(:rangeStart, NOW())) " +
            "AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd) " +
            "AND (:onlyAvailable IS NULL OR e.confirmedRequests > e.participantLimit) " +
            "AND e.stateAction = 'PUBLISHED'")
    List<Event> getEventsWithFiltration(@Param("text") String text,
                                        @Param("categories") Long[] categories,
                                        @Param("paid") Boolean paid,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                        @Param("onlyAvailable") Boolean onlyAvailable,
                                        Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE (:events IS NULL OR e.id IN :events)")
    Set<Event> getEventsById(@Param("events") Set<Long> events);
}