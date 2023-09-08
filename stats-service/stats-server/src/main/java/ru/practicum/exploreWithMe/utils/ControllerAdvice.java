package ru.practicum.exploreWithMe.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exploreWithMe.exception.DateTimeValidationException;

@RestControllerAdvice
@Slf4j
@Validated
public class ControllerAdvice {
    public static final String ERROR_400 = "Ошибка 400";
    public static final String ERROR_400_DESCRIPTION = "Ошибка валидации";

    @ExceptionHandler({DateTimeValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse fourHundredErrorHandle(final Exception exception) {
        log.warn(ERROR_400, exception);
        return new ErrorResponse(ERROR_400, ERROR_400_DESCRIPTION);
    }
}