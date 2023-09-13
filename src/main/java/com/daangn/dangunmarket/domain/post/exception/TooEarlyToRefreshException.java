package com.daangn.dangunmarket.domain.post.exception;

import lombok.Getter;

import java.time.Duration;

@Getter
public class TooEarlyToRefreshException extends RuntimeException {
    private final long remainingDays;
    private final long remainingHours;
    private final long remainingMinutes;

    public TooEarlyToRefreshException(Duration duration) {
        this.remainingDays = duration.toDaysPart();
        this.remainingHours = duration.toHoursPart();
        this.remainingMinutes = duration.toMinutesPart();
    }

}
