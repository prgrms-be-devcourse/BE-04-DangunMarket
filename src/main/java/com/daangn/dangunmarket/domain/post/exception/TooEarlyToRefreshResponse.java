package com.daangn.dangunmarket.domain.post.exception;

public record TooEarlyToRefreshResponse(
        boolean flag,
        long remainingDays,
        long remainingHours,
        long remainingMinutes) {

    public static TooEarlyToRefreshResponse of(
            long days,
            long hours,
            long minutes) {
        return new TooEarlyToRefreshResponse(
                false,
                days,
                hours,
                minutes
        );
    }

}
