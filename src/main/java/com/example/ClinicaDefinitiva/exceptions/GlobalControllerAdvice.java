package com.example.ClinicaDefinitiva.exceptions;


import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.persistence.entity.ErrorResponse;
import com.example.ClinicaDefinitiva.web.controller.TurnoController;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.example.ClinicaDefinitiva.Enum.CatalogoError.*;


@ControllerAdvice
public class GlobalControllerAdvice {


    // ERRORES DE NOT FOUND
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OdontologoNotfountException.class)
    public ErrorResponse handleOdontologoNotFoundException() {
        return new ErrorResponse(
                DENTIST_NOT_FOUND.getCode(),
                null,
                DENTIST_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND,

                LocalDateTime.now()
        );

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PacienteNotFountException.class)
    public ErrorResponse  handlePacienteNotFoundException(){
         return new ErrorResponse(
                 PATIENT_NOT_FOUND.getCode(),
                 null,
                 PATIENT_NOT_FOUND.getMessage(),
                 HttpStatus.NOT_FOUND,
                 LocalDateTime.now()

         );
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SecretarioNotFountException.class)
    public ErrorResponse handleSecretarioNotFoundException(){
        return new ErrorResponse(
                SECRETARY_NOT_FOUND.getCode(),
                null,
                SECRETARY_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()

        );
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResponsableNotFountException.class)
    public ErrorResponse handleResponsableNotFoundException(){
        return new ErrorResponse (
                RESPONSIBLE_NOT_FOUND.getCode(),
        null,
                RESPONSIBLE_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TurnoNotFountException.class)
    public ErrorResponse handleTurnoNotFoundException(){
       return new ErrorResponse(
               SHIFT_NOT_FOUND.getCode(),
               null,
               SHIFT_NOT_FOUND.getMessage(),
               HttpStatus.NOT_FOUND,
               LocalDateTime.now()
       );
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HorarioNotfountException.class)
    public ErrorResponse handleHorarioNotFoundException(){
        return new ErrorResponse(
                SCHEDULE_NOT_FOUND.getCode(),
                null,
                SCHEDULE_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsuarioNotfountException.class)
    public ErrorResponse handleUsuarioNotFoundException(){
        return new ErrorResponse(
                USER_NOT_FOUND.getCode(),
                null,
                USER_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }
    // ERRORES DE VALIDACIONES
  /*  @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentHorarioNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult result = exception.getBindingResult();

        // Crear instancia de ErrorResponse usando el constructor
        return new ErrorResponse(
                INVALID_SCHEDULE.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                INVALID_SCHEDULE.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }
    //@ControllerAdvice(assignableTypes = TurnoController.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentTurnoNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult result = exception.getBindingResult();

        // Crear instancia de ErrorResponse usando el constructor
        return new ErrorResponse(
                INVALID_SHIFT.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                INVALID_SHIFT.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentUsuarioNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult result = exception.getBindingResult();

        // Crear instancia de ErrorResponse usando el constructor
        return new ErrorResponse(
                INVALID_USER.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                INVALID_USER.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentOdontologoNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult result = exception.getBindingResult();

        // Crear instancia de ErrorResponse usando el constructor
        return new ErrorResponse(
                INVALID_DENTIST.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                INVALID_DENTIST.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentSecretarioNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult result = exception.getBindingResult();

        // Crear instancia de ErrorResponse usando el constructor
        return new ErrorResponse(
                INVALID_SECRETARY.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                INVALID_SECRETARY.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentPacienteNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult result = exception.getBindingResult();

        // Crear instancia de ErrorResponse usando el constructor
        return new ErrorResponse(
                INVALID_PATIENT.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                INVALID_PATIENT.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentResponsableNotValidException(
            MethodArgumentNotValidException exception) {

        BindingResult result = exception.getBindingResult();

        // Crear instancia de ErrorResponse usando el constructor
        return new ErrorResponse(
                INVALID_RESPONSIBLE.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                INVALID_RESPONSIBLE.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }*/

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String objectName = result.getObjectName();

        CatalogoError errorCode = switch (objectName) {
            case "HorarioDto"   -> CatalogoError.INVALID_SCHEDULE;
            case "TurnoDto"     -> CatalogoError.INVALID_SHIFT;
            case "UsuarioDto"   -> CatalogoError.INVALID_USER;
            case "createOdontologoDto" -> CatalogoError.INVALID_DENTIST;
            case "createPacienteDto" -> CatalogoError.INVALID_PATIENT;
            case "createSecretarioDto" -> CatalogoError.INVALID_SECRETARY;
            // añade aquí más casos según tus DTOs…
            default             -> CatalogoError.GENERIC_ERROR;
        };

        return new ErrorResponse(
                errorCode.getCode(),
                result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()),
                errorCode.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }



    // ERRORES GENERALES
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServeError(
            Exception exception) {
        return new ErrorResponse(
                GENERIC_ERROR.getCode(),
                Collections.singletonList(exception.getMessage()),
                GENERIC_ERROR.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
    }
}
