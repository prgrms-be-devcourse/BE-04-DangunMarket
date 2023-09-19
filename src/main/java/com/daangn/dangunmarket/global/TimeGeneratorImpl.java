package com.daangn.dangunmarket.global;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeGeneratorImpl implements TimeGenerator {

    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

}
