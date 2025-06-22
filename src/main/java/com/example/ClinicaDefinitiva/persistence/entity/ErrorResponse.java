package com.example.ClinicaDefinitiva.persistence.entity;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {


    private String code;
    private HttpStatus status;
    private String message;
    private List<String> detailMessages;
    private LocalDateTime timeStamp;

    public ErrorResponse(String code, List<String> detailMessages, String message, HttpStatus status, LocalDateTime timeStamp) {
        this.code = code;
        this.detailMessages = detailMessages;
        this.message = message;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getDetailMessages() {
        return detailMessages;
    }

    public void setDetailMessages(List<String> detailMessages) {
        this.detailMessages = detailMessages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
