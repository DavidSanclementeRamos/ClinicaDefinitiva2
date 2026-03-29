package com.example.ClinicaDefinitiva.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/* permitira construir respuestas de error reutilizables de forma clara, trazable
y profesional. Esto te libera de repetir lógica dentro de GlobalControllerAdvice
y organiza tus manejadores como una orquesta

🔹 Este utilitario tiene dos constructores:
- Uno para excepciones con EntityContext.
- Otro para casos genéricos como validaciones, JSON mal formado, tipo de parámetro, etc.

*/
public class ErrorHandlerUtils {

    public static String getUsuarioActual() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("anonimo");
    }

    public static String getRequestId() {
        return RequestIdFilter.getRequestId();
    }

    /**public static ErrorResponse construirError(ErrorCatalogXD error,
                                               EntityContext contexto,
                                               String mensajeDetalle,
                                               HttpStatus status) {
        return new ErrorResponse(
                error.getCode(),
                contexto.getCodigoEntidad().name(),
                contexto.name(),
                getUsuarioActual(),
                error.getMessage(),
                List.of(mensajeDetalle),
                status,
                LocalDateTime.now(),
                getRequestId()
        );
    }

    public static ErrorResponse construirError(ErrorCatalogXD error,
                                               String contextoRaw,
                                               List<String> detalles,
                                               HttpStatus status) {
        return new ErrorResponse(
                error.getCode(),
                "GENERIC",
                contextoRaw.toUpperCase(),
                getUsuarioActual(),
                error.getMessage(),
                detalles,
                status,
                LocalDateTime.now(),
                getRequestId()
        );
    }*/

}
