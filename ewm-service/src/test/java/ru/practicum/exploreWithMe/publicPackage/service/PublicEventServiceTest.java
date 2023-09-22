package ru.practicum.exploreWithMe.publicPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.publicPackage.service.eventService.PublicEventServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PublicEventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private PublicEventServiceImpl eventService;

    @Test
    void should() {

    }
}
