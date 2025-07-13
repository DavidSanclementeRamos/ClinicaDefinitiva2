package com.example.ClinicaDefinitiva.exceptions;


import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.*;
import com.example.ClinicaDefinitiva.persistence.entity.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.ClinicaDefinitiva.Enum.CatalogoError.*;


@RestControllerAdvice
public class GlobalControllerAdvice {


    // ERRORES DE NOT FOUND

    @ExceptionHandler(ClinicaDefinitivaException.class)
    public ResponseEntity<ErrorResponse> manejarError(ClinicaDefinitivaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                        ex.getCatalogo().getCode(),
                        List.of(ex.getMessage()),
                        ex.getCatalogo().getMessage(),
                        HttpStatus.BAD_REQUEST,
                        LocalDateTime.now(),
                        ex.getContexto().name()  // Contexto como string
                )
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String objectName = result.getObjectName();

        CatalogoError errorCode = switch (objectName) {
            case "HorarioDto"   -> CatalogoError.INVALID_SCHEDULE;
            case "TurnoDto"     -> CatalogoError.INVALID_SHIFT;
            case "UsuarioDto"   -> CatalogoError.INVALID_USER;
            case "CreateOdontologoDto" -> CatalogoError.INVALID_DENTIST;
            case "CreatePacienteDto" -> CatalogoError.INVALID_PATIENT;
            case "CreateSecretarioDto" -> CatalogoError.INVALID_SECRETARY;
            case "CreateEndReadResponsable" -> INVALID_RESPONSIBLE;
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
    // Esto te permite devolver un error 415 Unsupported Media Type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return new ErrorResponse(
                CatalogoError.UNSUPPORTED_MEDIA_TYPE.getCode(), // o reemplaza por tu código
                Collections.singletonList("El tipo de contenido no es compatible: " + ex.getContentType()),
                "Tipo de contenido no soportado.",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                LocalDateTime.now()
        );
    }

    // Para capturarlo y devolver un 405 Method Not Allowed:
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        String mensaje = String.format(
                "El método %s no está soportado. Métodos válidos: %s",
                ex.getMethod(),
                String.join(", ", Objects.requireNonNull(ex.getSupportedHttpMethods()).stream()
                        .map(HttpMethod::name)
                        .toList())
        );
        return new ErrorResponse(
                CatalogoError.METHOD_NOT_ALLOWED.getCode(),
                List.of(mensaje),
                CatalogoError.METHOD_NOT_ALLOWED.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED,
                LocalDateTime.now()
        );
    }


    //  Para devolver un 400 Bad Request bien explicado: MissingServletRequestParameterException
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParams(MissingServletRequestParameterException ex) {
        String mensaje = String.format(
                "Falta el parámetro '%s' de tipo %s",
                ex.getParameterName(),
                ex.getParameterType()
        );
        return new ErrorResponse(
                CatalogoError.MISSING_REQUEST_PARAM.getCode(),
                List.of(mensaje),
                CatalogoError.MISSING_REQUEST_PARAM.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    // Se dispara cuando el cuerpo JSON está mal formado o no puede deserializarse.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String detalle = "Cuerpo JSON mal formado: " + ex.getMostSpecificCause().getMessage();
        return new ErrorResponse(
                CatalogoError.INVALID_JSON.getCode(),
                List.of(detalle),
                CatalogoError.INVALID_JSON.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    // Atrapamos violaciones de restricciones en parámetros de ruta o query params (por ejemplo, con @Validated).
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        List<String> detalles = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();

        return new ErrorResponse(
                CatalogoError.INVALID_PARAMETERS.getCode(),
                detalles,
                CatalogoError.INVALID_PARAMETERS.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    // Se lanza cuando la ruta solicitada no está mapeada en ningún controlador
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(NoHandlerFoundException ex) {
        String detalle = "La ruta " + ex.getHttpMethod() + " " + ex.getRequestURL() + " no existe";
        return new ErrorResponse(
                CatalogoError.ROUTE_NOT_FOUND.getCode(),
                List.of(detalle),
                CatalogoError.ROUTE_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }


    // - MethodArgumentTypeMismatchException
    //Captura errores cuando un parámetro de consulta o ruta no coincide
    // con el tipo esperado (por ejemplo, enviar texto en lugar de número).
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String detalle = String.format(
                "Parámetro '%s' con valor '%s' no válido. Se esperaba tipo %s",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()
        );
        return new ErrorResponse(
                CatalogoError.TYPE_MISMATCH.getCode(),
                List.of(detalle),
                CatalogoError.TYPE_MISMATCH.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    // acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        String detalle = "No tienes permiso para acceder a este recurso.";
        return new ErrorResponse(
                CatalogoError.ACCESS_DENIED.getCode(),
                List.of(detalle),
                CatalogoError.ACCESS_DENIED.getMessage(),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now()
        );
    }

    // violacion de integridad de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String detalle = "Violación de restricciones de la base de datos: " + Objects.requireNonNull(ex.getRootCause()).getMessage();
        return new ErrorResponse(
                CatalogoError.DATA_INTEGRITY_VIOLATION.getCode(),
                List.of(detalle),
                CatalogoError.DATA_INTEGRITY_VIOLATION.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );

    }

    @ExceptionHandler(EdadNoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarEdadNoPermitida(EdadNoPermitidaException ex) {
        return new ErrorResponse(
                CatalogoError.EDAD_NO_PERMITIDA.getCode(),
                List.of(ex.getMessage()),
                CatalogoError.EDAD_NO_PERMITIDA.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

}
