package ru.practicum.exploreWithMe.commonFiles.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.ExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    public static final String ERROR_400 = "Ошибка 400";
    public static final String ERROR_404 = "Ошибка 404";
    public static final String ERROR_409 = "Ошибка 409";
    public static final String ERROR_500 = "Ошибка 500";

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError fourHundredErrorHandle(final Exception exception) {
        log.warn(ERROR_400, exception);
        return createApiError(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ExistException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError fourHundredFourErrorHandle(final Exception exception) {
        log.warn(ERROR_404, exception);
        return createApiError(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EventDateTimeException.class, EventLimitException.class, EventNotPublicisedException.class,
            EventUpdateStateException.class, RequestChangeNotPendingStatusException.class,
            RequestRepeatException.class, UserIsNotEventInitiatorException.class, UserIsNotRequesterException.class,
            CategoryDuplicateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError fourHundredNineErrorHandle(final Exception exception) {
        log.warn(ERROR_409, exception);
        return createApiError(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError fiveHundredErrorHandle(final Throwable exception) {
        log.warn(ERROR_500, exception);
        return createApiError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError createApiError(Throwable exception, HttpStatus status) {
        return new ApiError(
                getStackTrace(exception),
                exception.getMessage(),
                status.getReasonPhrase(),
                status,
                LocalDateTime.now());
    }

    private List<String> getStackTrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        return Arrays.asList(stackTrace.split("\\r?\\n"));
    }
}
