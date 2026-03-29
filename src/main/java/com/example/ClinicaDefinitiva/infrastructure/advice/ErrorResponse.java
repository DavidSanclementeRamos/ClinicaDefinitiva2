package com.example.ClinicaDefinitiva.infrastructure.advice;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {


    private String code;
    private String codigoEntidad;
    private String contexto;
    private String usuario;
    private String message;
    private List<String> detailMessages;
    private HttpStatus status;
    private LocalDateTime timeStamp;
    private String requestId;

    public ErrorResponse(String code,
                         String codigoEntidad,
                         String contexto,
                         String usuario,
                         String message,
                         List<String> detailMessages,
                         HttpStatus status,
                         LocalDateTime timeStamp,
                         String requestId) {
        this.code = code;
        this.codigoEntidad = codigoEntidad;
        this.contexto = contexto;
        this.usuario = usuario;
        this.message = message;
        this.detailMessages = detailMessages;
        this.status = status;
        this.timeStamp = timeStamp;
        this.requestId = requestId;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
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
