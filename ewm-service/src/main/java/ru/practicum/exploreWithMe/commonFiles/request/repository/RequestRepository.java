package ru.practicum.exploreWithMe.commonFiles.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.commonFiles.request.model.Request;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.event.id = :eventId")
    List<Request> getEventRequests(@Param("eventId") Long eventId);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.id IN :requestSet")
    List<Request> findAllByRequestIdList(@Param("requestSet") Set<Long> requestSet);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.requester.id = :userId")
    List<Request> getUserRequests(@Param("userId") Long userId);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.event.id = :eventId " +
            "AND r.requester.id = :userId")
    Optional<Request> getUserEventRequest(@Param("eventId") Long eventId,
                                          @Param("userId") Long userId);
}
