package ru.practicum.exploreWithMe.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    public static final String ERROR_400 = "Ошибка 400";
    public static final String ERROR_400_DESCRIPTION = "Ошибка валидации";

    public static final String ERROR_500 = "Unknown state: UNSUPPORTED_STATUS";
    public static final String ERROR_500_DESCRIPTION = "Возникло исключение";

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse fourHundredErrorHandle(final Exception exception) {
        log.warn(ERROR_400, exception);
        return new ErrorResponse(ERROR_400, ERROR_400_DESCRIPTION);
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse fiveHundredErrorHandle(final Exception exception) {
        log.warn(ERROR_500, exception);
        return new ErrorResponse(ERROR_500, ERROR_500_DESCRIPTION);
    }
}