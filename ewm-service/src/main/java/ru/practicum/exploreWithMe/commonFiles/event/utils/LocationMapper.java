package ru.practicum.exploreWithMe.commonFiles.event.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.commonFiles.event.dto.LocationDto;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;

@UtilityClass
@Slf4j
public class LocationMapper {
    public Location toLocation(LocationDto locationDto) {
        log.info("Начата процедура преобразования локации в ДТО: {}", locationDto);
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public LocationDto toDto(Location location) {
        log.info("Начата процедура преобразования ДТО в локацию: {}", location);
        return new LocationDto(location.getLat(), location.getLon());
    }
}
