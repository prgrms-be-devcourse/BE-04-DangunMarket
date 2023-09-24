package com.daangn.dangunmarket.domain.chat.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class MessageControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(MessageControllerAdvice.class);

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendToUser("/queue/errors")
    public MessageErrorDto methodArgumentNotValidWebSocketExceptionHandler(
            MethodArgumentNotValidException e) {
        log.info(e.getMessage());
        return new MessageErrorDto("fail", e.getMessage());
    }
}
