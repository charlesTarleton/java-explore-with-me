package ru.practicum.exploreWithMe.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.exploreWithMe.dto.EndpointHitDto;

@Slf4j
@RequiredArgsConstructor
//@Service
public class StatsClient {
    @Value("${client.api.url}")
    private final String url;
    private static final String CLIENT_LOG = "Клиент статистики получил запрос на {}{}";
    private final RestTemplate rest;

    public ResponseEntity<Object> saveStatistic(EndpointHitDto endpointHitDto) {
        log.info(CLIENT_LOG, "сохранение элемента статистики: ", endpointHitDto);
        return makeAndSendRequest(HttpMethod.POST, url + "/hit", null, endpointHitDto);
    }

    public ResponseEntity<Object> getStatistic(LocalDateTime start, LocalDateTime end,
                                                  String[] uris, Boolean unique) {
        log.info(CLIENT_LOG, "получение элементов статистики с параметрами:",
                "\nstart " + start + "\nend " + end + "\nuris " + Arrays.toString(uris) + "\nunique" + unique);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startStr = start.format(formatter);
        String endStr = end.format(formatter);
        return makeAndSendRequest(HttpMethod.GET, url + "/stats",
                Map.of("start", startStr, "end", endStr, "uris", uris, "unique", unique),
                null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                          String path,
                                                          @Nullable Map<String, Object> parameters,
                                                          @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, null);

        ResponseEntity<Object> ewmServerResponse;
        try {
            if (parameters != null) {
                ewmServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                ewmServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareStatisticResponse(ewmServerResponse);
    }

    private static ResponseEntity<Object> prepareStatisticResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}