package ru.practicum.exploreWithMe.commonFiles.exception.fourHundred;

public class EventDateTimePastException extends RuntimeException {
    public EventDateTimePastException(String message) {
        super(message);
    }
}
