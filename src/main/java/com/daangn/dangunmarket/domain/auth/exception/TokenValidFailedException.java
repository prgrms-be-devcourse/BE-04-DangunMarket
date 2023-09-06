package com.daangn.dangunmarket.domain.auth.exception;

public class TokenValidFailedException extends IllegalStateException {

    public TokenValidFailedException(String message) {
        super(message);
    }

}
