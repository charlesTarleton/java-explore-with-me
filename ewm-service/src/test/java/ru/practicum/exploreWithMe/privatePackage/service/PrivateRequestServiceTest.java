package ru.practicum.exploreWithMe.privatePackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.commonFiles.request.repository.RequestRepository;
import ru.practicum.exploreWithMe.privatePackage.service.requestService.PrivateRequestServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PrivateRequestServiceTest {
    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private PrivateRequestServiceImpl requestService;

    @Test
    void should() {

    }
}
