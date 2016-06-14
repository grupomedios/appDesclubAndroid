package com.grupomedios.desclub.desclubutil.exception;

public class DesclubException extends RuntimeException {

    public DesclubException(String message) {
        super(message);
    }

    public DesclubException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}