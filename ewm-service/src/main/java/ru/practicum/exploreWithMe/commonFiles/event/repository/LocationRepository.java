package ru.practicum.exploreWithMe.commonFiles.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l " +
            "FROM Location AS l " +
            "WHERE l.lat = :lat AND l.lon = :lon")
    Optional<Location> findById(@Param("lat") Float lat,
                                @Param("lon") Float lon);
}
