package com.example.ClinicaDefinitiva.infrastructure.rest.advice;


import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ClinicaDefinitivaException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.EdadNoPermitidaException;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.ErrorResponse;
import com.example.ClinicaDefinitiva.util.ErrorCodeResolver;
import com.example.ClinicaDefinitiva.util.ErrorHandlerUtils;
import com.example.ClinicaDefinitiva.util.RequestIdFilter;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import java.util.List;
import static com.example.ClinicaDefinitiva.util.RequestIdFilter.getRequestId;


@RestControllerAdvice
public class GlobalControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    // ERRORES DE NOT FOUND

    /*Esto significa que cualquier excepción que herede de ClinicaDefinitivaException
    (como OdontologoNotfountException) será capturada por este método sin necesidad
    de crear un handler por cada tipo.*/

    @ExceptionHandler(ClinicaDefinitivaException.class)
    public ResponseEntity<ErrorResponse> manejarExcepciones(ClinicaDefinitivaException ex) {

        /*Esta línea extrae el usuario activo si estás usando Spring Security.
         Si no tienes seguridad aún, simplemente devolverá "anonimo".
         No es obligatorio, pero prepara tu sistema para auditoría futura.
         */

        String usuario = ErrorHandlerUtils.getUsuarioActual();

        // 🔍 Registrar el error en logs
        logger.error("Error capturado [requestId={}]: código={}, contexto={}, usuario={}, detalle={}",
                ex.getRequestId(),
                ex.getCatalogo().getCode(),
                ex.getContexto().name(),
                usuario,
                ex.getMessage()
        );

        /*Este objeto encapsula TODO lo que un frontend moderno, un logger estructurado
        o una herramienta de monitoreo necesita para entender el error*/
        ErrorResponse response = new ErrorResponse(

                ex.getCatalogo().getCode(),                   // Código estandarizado (ERR_DENTIST_NOT_FOUND)
                ex.getContexto().getCodigoEntidad().name(),   // Código lógico del módulo (OD01)
                ex.getContexto().name(),                      // Contexto semántico ("ODONTOLOGO")
                usuario,                                      // Usuario autenticado o "anonimo"
                ex.getCatalogo().getMessage(),                // Mensaje base del catálogo
                List.of(ex.getMessage()),                     // Detalle dinámico (ej: "No se encontró el odontólogo con ID 12")
                HttpStatus.BAD_REQUEST,                       // Código HTTP (ajustable según error)
                LocalDateTime.now(),                          // Marca de tiempo
                ex.getRequestId()                             // ID único de la solicitud (para trazabilidad)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones(MethodArgumentNotValidException ex) {
       //Class<?> dtoClass = ex.getBindingResult().getTarget().g
        Object target = ex.getBindingResult().getTarget();
        Class<?> dtoClass = (target != null) ? target.getClass() : null;

            ErrorCatalogXD error = ErrorCodeResolver.resolver(dtoClass);

            logger.warn("No se pudo determinar la clase del DTO. Usando error genérico.");



        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .toList();

        assert dtoClass != null;
        ErrorResponse response = ErrorHandlerUtils.construirError(
                 error, dtoClass.getSimpleName(), detalles, HttpStatus.BAD_REQUEST
        );

        return ResponseEntity.badRequest().body(response);
    }


    // ERRORES GENERALES
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionInterna(Exception ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("Error interno inesperado [usuario={}, requestId={}]: {}", usuario, requestId, ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.GENERIC_ERROR.getCode(), "GENERIC", "EXCEPCION", usuario,
                ErrorCatalogXD.GENERIC_ERROR.getMessage(),
                List.of(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(), requestId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    // Esto te permite devolver un error 415 Unsupported Media Type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("El tipo de contenido no es compatible [usuario={}," + " requestId={}]: {}",
                usuario,
                requestId,
                ex.getMessage());



        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.UNSUPPORTED_MEDIA_TYPE.getCode(), // Código estandarizado (UNSUPPORTED_MEDIA_TYPE)
                "UNSUPPORTED",// Código lógico del módulo (OD01)
                "EXCEPTION",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.UNSUPPORTED_MEDIA_TYPE.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,// Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)


        );
         return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    // Para capturarlo y devolver un 405 Method Not Allowed:
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("El método %s no está soportado. Métodos válidos: %s[usuario={}," + " requestId={}]: {}",
                usuario,
                requestId,
                ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.METHOD_NOT_ALLOWED.getCode(), // Código estandarizado (METHOD_NOT_ALLOWED)
                "MISSING_REQUEST_PARAM",// Código lógico del módulo (OD01)
                "METHOD_NOT_SUPPORTED_EXCEPTION",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.METHOD_NOT_ALLOWED.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.METHOD_NOT_ALLOWED,// Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)


        );
       return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }


    //  Para devolver un 400 Bad Request bien explicado: MissingServletRequestParameterException
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {

        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("Falta el parámetro '%s' de tipo %s: %s[usuario={}," + " requestId={}]: {}",
                usuario,
                requestId,
                ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.MISSING_REQUEST_PARAM.getCode(), // Código estandarizado (METHOD_NOT_ALLOWED)
                "PARAM",// Código lógico del módulo (OD01)
                "MISSING_REQUEST_PARAM",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.MISSING_REQUEST_PARAM.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.BAD_REQUEST, // Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Se dispara cuando el cuerpo JSON está mal formado o no puede deserializarse.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("Cuerpo JSON mal formado: %s[usuario={}," + " requestId={}]: {}",
                usuario,
                requestId,
                ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.INVALID_JSON.getCode(), // Código estandarizado (METHOD_NOT_ALLOWED)
                "JSON",// Código lógico del módulo (OD01)
                "INVALID_JSON",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.INVALID_JSON.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.BAD_REQUEST, // Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Atrapamos violaciones de restricciones en parámetros de ruta o query params (por ejemplo, con @Validated).
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("Violacion de parmetro: %s[usuario={}," + " requestId={}]: {}",
                usuario,
                requestId,
                ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.INVALID_PARAMETERS.getCode(), // Código estandarizado (METHOD_NOT_ALLOWED)
                "VAL99",// Código lógico del módulo (OD01)
                "validation",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.INVALID_PARAMETERS.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.BAD_REQUEST, // Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }




    // Se lanza cuando la ruta solicitada no está mapeada en ningún controlador
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("La ruta {} {} no existen %s[usuario={}, requestId={}]: {}", ex.getHttpMethod(), ex.getRequestURL(), usuario, requestId, ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.ROUTE_NOT_FOUND.getCode(), // Código estandarizado (METHOD_NOT_ALLOWED)
                "NOHANDLER",// Código lógico del módulo (OD01)
                "RUTA_MAPEADA",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.ROUTE_NOT_FOUND.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.BAD_REQUEST, // Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // - MethodArgumentTypeMismatchException
    //Captura errores cuando un parámetro de consulta o ruta no coincide
    // con el tipo esperado (por ejemplo, enviar texto en lugar de número).
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse>handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("Parámetro {} con valor {} no válido. Se esperaba tipo {} %s[usuario={}," + " requestId={}]: ", ex.getParameter(), ex.getValue(), ex.getMessage(),
                usuario,
                requestId);

        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.TYPE_MISMATCH.getCode(), // Código estandarizado (METHOD_NOT_ALLOWED)
                "PARAMETRO NO VALIDO",// Código lógico del módulo (OD01)
                "HAY TEXTO EN LUGAR DE NUMERO",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.TYPE_MISMATCH.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.BAD_REQUEST,// Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)


        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);


    }
    // acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> manejarAccesoDenegado(AccessDeniedException ex) {
        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = RequestIdFilter.getRequestId();

        logger.warn("Acceso denegado [usuario={}, requestId={}]", usuario, requestId);

        ErrorResponse response = new ErrorResponse(
                "ERR_ACCESO_403",
                "GENERIC",
                "SEGURIDAD",
                usuario,
                "Acceso denegado a la operación solicitada",
                List.of(ex.getMessage()),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // violacion de integridad de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String usuario = ErrorHandlerUtils.getUsuarioActual();
        String requestId = getRequestId();

        logger.error("Violación de restricciones de la base de datos: %s[usuario={}," + " requestId={}]: {}",
                usuario,
                requestId,
                ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                ErrorCatalogXD.DATA_INTEGRITY_VIOLATION.getCode(), // Código estandarizado (METHOD_NOT_ALLOWED)
                "BDD",// Código lógico del módulo (OD01)
                "VIOLACION_BT",  // Contexto semántico ("EXCEPTION")
                usuario,  // Usuario autenticado o "anonimo"
                ErrorCatalogXD.DATA_INTEGRITY_VIOLATION.getMessage(), // Mensaje base del catálogo
                List.of(ex.getMessage()), // Detalle dinámico (ej:
                HttpStatus.CONFLICT,// Código HTTP (ajustable según error)
                LocalDateTime.now(), // Marca de tiempo
                requestId // ID único de la solicitud (para trazabilidad)

        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(EdadNoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> manejarEdadNoPermitida(EdadNoPermitidaException ex) {
       String usuario = ErrorHandlerUtils.getUsuarioActual();
       /* String usuario = Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication()
        ).map(auth -> auth.getName()).orElse("anonimo");*/

        // 🔍 Registrar el error en logs
        logger.error("Error capturado [requestId={}]: código={}, contexto={}, usuario={}, detalle={}",
                ex.getRequestId(),
                ex.getCatalogo().getCode(),
                ex.getContexto().name(),
                usuario,
                ex.getMessage()
        );
        ErrorResponse response = new ErrorResponse(

                ex.getCatalogo().getCode(),                   // Código estandarizado (ERR_DENTIST_NOT_FOUND)
                ex.getContexto().getCodigoEntidad().name(),   // Código lógico del módulo (OD01)
                ex.getContexto().name(),                      // Contexto semántico ("ODONTOLOGO")
                usuario,                                      // Usuario autenticado o "anonimo"
                ex.getCatalogo().getMessage(),                // Mensaje base del catálogo
                List.of(ex.getMessage()),                     // Detalle dinámico (ej: "No se encontró el odontólogo con ID 12")
                HttpStatus.BAD_REQUEST,                       // Código HTTP (ajustable según error)
                LocalDateTime.now(),                          // Marca de tiempo
                ex.getRequestId()                             // ID único de la solicitud (para trazabilidad)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
