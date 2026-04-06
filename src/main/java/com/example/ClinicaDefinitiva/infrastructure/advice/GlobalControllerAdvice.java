package com.example.ClinicaDefinitiva.infrastructure.advice;

import com.example.ClinicaDefinitiva.application.exceptions.actor.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actor.GuardianNoFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actor.PatientNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actor.ReceptionistNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.AdministrativeReportNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.ContractNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.JournalEntryNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.LedgerAccountNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.OpeningBalanceNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.ThirdPartyNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.UserRolAssignmentNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.operations.ShiftNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.authentication.UserIdentityNoFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.billing.InvoiceNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.billing.RateNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.clinicalTreatments.TreatmentNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.dentalService.ProvidedServiceNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.scheduled.AppointmentNotFoundException;
import com.example.ClinicaDefinitiva.domain.errors.catalog.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.GuardianError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.ReceptionistError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.AdministrativeReportError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.CompanyError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.ContractError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.JournalEntryError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.LedgerAccountError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.OpeningBalanceError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.ThirdPartiesError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.operations.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.AuthenticationVoError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.catalog.billing.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.clinicalTreatments.TreatmentError; 


import com.example.ClinicaDefinitiva.domain.exceptions.*;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ClinicaDefinitiva.util.RequestIdFilter.getRequestId;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Manejador global de excepciones para la capa de infraestructura REST.
 * Traduce las excepciones del dominio y de infraestructura a respuestas HTTP
 * estructuradas según el catálogo de errores clínicos.
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    // ==================== ERRORES DE DOMINIO ====================

    /**
     * Maneja violaciones de reglas de negocio simples.
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolation(BusinessRuleViolationException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("Regla de negocio violada [requestId={}, usuario={}, código={}, contexto={}]: {}",
                requestId, usuario, ex.getCatalogo().getCode(), ex.getContexto().getCodeEntity().name(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                ex.getCatalogo().getCode(),
                ex.getContexto().getCodeEntity().toString(),
                ex.getContexto().getCodeEntity().name(),
                usuario,
                ex.getCatalogo().getDefaultMessage(),
                List.of(ex.getMessage()),
                ex.getCatalogo().getSuggestedHttpStatus(),
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(ex.getCatalogo().getSuggestedHttpStatus()).body(response);
    }

    /**
     * Maneja múltiples violaciones de reglas de negocio (agregadas).
     */
    @ExceptionHandler(AggregateBusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleAggregateBusinessRuleViolation(AggregateBusinessRuleViolationException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        List<String> detalles = ex.getDetalles().stream()
                .map(detail -> detail.getCode().getCode())
                .collect(Collectors.toList());

        logger.warn("Múltiples reglas de negocio violadas [requestId={}, usuario={}, totalViolaciones={}]: {}",
                requestId, usuario, ex.getTotalViolaciones(), detalles);

        ErrorResponse response = new ErrorResponse(
                ex.getCatalogo().getCode(),
ex.getContexto().getCodeEntity().toString(),
                ex.getContexto().getCodeEntity().name(),
                usuario,
                ex.getCatalogo().getDefaultMessage(),
                detalles,
                ex.getCatalogo().getSuggestedHttpStatus(),
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(ex.getCatalogo().getSuggestedHttpStatus()).body(response);
    }

    /**
     * Maneja errores de validación de Value Objects.
     */
    @ExceptionHandler(ValueObjectValidationException.class)
    public ResponseEntity<ErrorResponse> handleValueObjectValidation(ValueObjectValidationException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("Validación de Value Object falló [requestId={}, usuario={}, código={}]: {}",
                requestId, usuario, ex.getCatalogo().getCode(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                ex.getCatalogo().getCode(),
ex.getContexto().getCodeEntity().toString(),
                ex.getContexto().getCodeEntity().name(),
                usuario,
                ex.getCatalogo().getDefaultMessage(),
                List.of(ex.getMessage()),
                ex.getCatalogo().getSuggestedHttpStatus(),
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(ex.getCatalogo().getSuggestedHttpStatus()).body(response);
    }

    // ==================== ERRORES DE VALIDACIÓN ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        logger.warn("Validación de DTO falló [requestId={}, usuario={}, detalles={}]",
                requestId, usuario, detalles);

        ErrorResponse response = new ErrorResponse(
                "ERR_VALIDATION_001",
                "VAL",
                "VALIDATION",
                usuario,
                "Error de validación en los datos de entrada",
                detalles,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        List<String> detalles = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        logger.warn("Violación de constraint [requestId={}, usuario={}, detalles={}]",
                requestId, usuario, detalles);

        ErrorResponse response = new ErrorResponse(
                "ERR_VALIDATION_002",
                "VAL",
                "VALIDATION",
                usuario,
                "Error de validación en parámetros de la URL",
                detalles,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        String mensaje = String.format("Parámetro '%s' con valor '%s' no es válido. Se esperaba tipo: %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        logger.warn("Type mismatch [requestId={}, usuario={}]: {}", requestId, usuario, mensaje);

        ErrorResponse response = new ErrorResponse(
                "ERR_TYPE_MISMATCH",
                "PARAM",
                "VALIDATION",
                usuario,
                "Tipo de dato incorrecto en parámetro",
                List.of(mensaje),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("JSON mal formado [requestId={}, usuario={}]: {}", requestId, usuario, ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "ERR_INVALID_JSON",
                "JSON",
                "VALIDATION",
                usuario,
                "El cuerpo de la petición tiene un formato JSON inválido",
                List.of(ex.getMessage()),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("Parámetro faltante [requestId={}, usuario={}]: {}", requestId, usuario, ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "ERR_MISSING_PARAM",
                "PARAM",
                "VALIDATION",
                usuario,
                "Falta un parámetro requerido",
                List.of(ex.getMessage()),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.badRequest().body(response);
    }

    // ==================== ERRORES DE SEGURIDAD ====================

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("Acceso denegado [requestId={}, usuario={}]: {}", requestId, usuario, ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "ERR_ACCESS_DENIED",
                "AUTH",
                "SECURITY",
                usuario,
                "No tiene permisos para realizar esta operación",
                List.of(ex.getMessage()),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // ==================== ERRORES DE INTEGRIDAD DE DATOS ====================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        String mensajeError = ex.getMostSpecificCause().getMessage();
        String mensajeAmigable;
        ErrorCatalog error;
        DomainContext context;
        HttpStatus status = HttpStatus.CONFLICT;

        if (mensajeError.contains("Duplicate entry") || mensajeError.contains("duplicate key")) {
            IntegrityErrorInfo info = parseDuplicateKeyError(mensajeError);
            error = info.error;
            context = info.context;
            mensajeAmigable = info.mensaje;
            status = HttpStatus.CONFLICT;
            logger.warn("Violación de unicidad [requestId={}, usuario={}, recurso={}]: {}",
                    requestId, usuario, context.getCodeEntity().name(), mensajeError);
        } else if (mensajeError.contains("cannot be null") || mensajeError.contains("NOT NULL")) {
            IntegrityErrorInfo info = parseNotNullError(mensajeError);
            error = info.error;
            context = info.context;
            mensajeAmigable = info.mensaje;
            status = HttpStatus.BAD_REQUEST;
            logger.warn("Campo obligatorio nulo [requestId={}, usuario={}, recurso={}]: {}",
                    requestId, usuario, context.getCodeEntity().name(), mensajeError);
        } else if (mensajeError.contains("foreign key") || mensajeError.contains("FOREIGN KEY")) {
            IntegrityErrorInfo info = parseForeignKeyError(mensajeError);
            error = info.error;
            context = info.context;
            mensajeAmigable = info.mensaje;
            status = HttpStatus.CONFLICT;
            logger.warn("Violación de clave foránea [requestId={}, usuario={}, recurso={}]: {}",
                    requestId, usuario, context.getCodeEntity().name(), mensajeError);
        } else if (mensajeError.contains("Data too long")) {
            IntegrityErrorInfo info = parseDataTooLongError(mensajeError);
            error = info.error;
            context = info.context;
            mensajeAmigable = info.mensaje;
            status = HttpStatus.BAD_REQUEST;
            logger.warn("Longitud de campo excedida [requestId={}, usuario={}, recurso={}]: {}",
                    requestId, usuario, context.getCodeEntity().name(), mensajeError);
        } else {
            error = new GenericErrorCatalog(
                    "ERR_DATA_INTEGRITY",
                    "Error de integridad de datos",
                    HttpStatus.CONFLICT,
                    ErrorSeverity.ERROR
            );
            context = EntityContext.SYSTEM;
            mensajeAmigable = "Violación de integridad de datos: " + mensajeError;
            logger.error("Violación de integridad no clasificada [requestId={}, usuario={}]: {}",
                    requestId, usuario, mensajeError);
        }

        ErrorResponse response = new ErrorResponse(
                error.getCode(),
                context.getCodeEntity().toString(),
                context.getCodeEntity().name(),
                usuario,
                error.getDefaultMessage(),
                List.of(mensajeAmigable),
                status,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(status).body(response);
    }

    // ==================== ERRORES DE RECURSO NO ENCONTRADO ====================

    @ExceptionHandler({
            // Actor
            DentistNotFoundException.class,
            GuardianNoFoundException.class,
            PatientNotFoundException.class,
            ReceptionistNotFoundException.class,
            // Accounting
            AdministrativeReportNotFoundException.class,
            CompanyNotFoundException.class,
            ContractNotFoundException.class,
            JournalEntryNotFoundException.class,
            LedgerAccountNotFoundException.class,
            OpeningBalanceNotFoundException.class,
            ThirdPartyNotFoundException.class,
            // Authorization
            RolNotFoundException.class,
            UserRolAssignmentNotFoundException.class,
            // Operations
            ShiftNotFoundException.class,
            // Authentication
            UserIdentityNoFoundException.class,
            // Billing
            InvoiceNotFoundException.class,
            RateNotFoundException.class,
            // Clinical Treatments
            TreatmentNotFoundException.class,
            // Dental Service
            ProvidedServiceNotFoundException.class,
            // Schedule
            AppointmentNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        ErrorCatalog error = getErrorCatalogForException(ex);
        DomainContext context = getContextForException(ex);

        logger.warn("Recurso no encontrado [requestId={}, usuario={}, tipo={}]: {}",
                requestId, usuario, ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                error.getCode(),
                context.getCodeEntity().toString(),
                context.getCodeEntity().name(),
                usuario,
                error.getDefaultMessage(),
                List.of(ex.getMessage()),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ==================== ERRORES HTTP ====================

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("Ruta no encontrada [requestId={}, usuario={}]: {} {}",
                requestId, usuario, ex.getHttpMethod(), ex.getRequestURL());

        ErrorResponse response = new ErrorResponse(
                "ERR_NOT_FOUND",
                "ROUTE",
                "RESOURCE",
                usuario,
                "La ruta solicitada no existe",
                List.of(ex.getRequestURL()),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("Método no soportado [requestId={}, usuario={}]: {}", requestId, usuario, ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "ERR_METHOD_NOT_ALLOWED",
                "HTTP",
                "PROTOCOL",
                usuario,
                "El método HTTP no está soportado para esta ruta",
                List.of(ex.getMessage()),
                HttpStatus.METHOD_NOT_ALLOWED,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.warn("Media type no soportado [requestId={}, usuario={}]: {}", requestId, usuario, ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "ERR_UNSUPPORTED_MEDIA_TYPE",
                "HTTP",
                "PROTOCOL",
                usuario,
                "El tipo de contenido no está soportado",
                List.of(ex.getMessage()),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    // ==================== FALLBACK ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        String usuario = getCurrentUser();
        String requestId = getRequestId();

        logger.error("Error interno no manejado [requestId={}, usuario={}]", requestId, usuario, ex);

        ErrorResponse response = new ErrorResponse(
                "ERR_INTERNAL_SERVER",
                "SYS",
                "SYSTEM",
                usuario,
                "Error interno del servidor",
                List.of(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // ==================== UTILIDADES ====================

    private String getCurrentUser() {
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder
                    .getContext()
                    .getAuthentication();
            return auth != null ? auth.getName() : "anonymous";
        } catch (Exception e) {
            return "anonymous";
        }
    }

    // -------------------- Mapeos para DataIntegrityViolationException --------------------

    private static class IntegrityErrorInfo {
        final ErrorCatalog error;
        final DomainContext context;
        final String mensaje;

        IntegrityErrorInfo(ErrorCatalog error, DomainContext context, String mensaje) {
            this.error = error;
            this.context = context;
            this.mensaje = mensaje;
        }
    }

    private IntegrityErrorInfo parseDuplicateKeyError(String mensaje) {
        if (mensaje.contains("uk_correo_electronico") || mensaje.contains("correo_electronico")) {
            return new IntegrityErrorInfo(
                    AuthenticationVoError.ERR_USER_DUPLICATE_EMAIL,
                    EntityContext.USER_IDENTITY,
                    "El correo electrónico ya está registrado en el sistema"
            );
        }
        if (mensaje.contains("uk_documento") || mensaje.contains("documento")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_DUPLICATE_DOCUMENT", "Documento ya registrado", HttpStatus.CONFLICT, ErrorSeverity.ERROR),
                    EntityContext.THISPARTIES,
                    "El número de documento ya existe en el sistema"
            );
        }
        if (mensaje.contains("uk_nit") || mensaje.contains("nit")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_DUPLICATE_NIT", "NIT ya registrado", HttpStatus.CONFLICT, ErrorSeverity.ERROR),
                    EntityContext.COMPANY,
                    "El NIT de la empresa ya está registrado"
            );
        }
        if (mensaje.contains("uk_nombre") || mensaje.contains("nombre")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_DUPLICATE_NAME", "Nombre ya registrado", HttpStatus.CONFLICT, ErrorSeverity.ERROR),
                    EntityContext.GENERIC,
                    "El nombre ya existe en el sistema"
            );
        }
        if (mensaje.contains("uk_codigo") || mensaje.contains("código")) {
            return new IntegrityErrorInfo(
                    ServiceVOError.ERR_SERVICE_CODE_DUPLICATE,
                    EntityContext.DENTAL_SERVICE,
                    "El código del servicio ya existe"
            );
        }
        if (mensaje.contains("uk_descripcion") || mensaje.contains("descripcion")) {
            return new IntegrityErrorInfo(
                    RolError.ERR_ROL_DUPLICATE_DESCRIPTION,
                    EntityContext.ROL,
                    "Ya existe un rol con esta descripción"
            );
        }
        if (mensaje.contains("uk_numero_factura") || mensaje.contains("numero_factura")) {
            return new IntegrityErrorInfo(
                    InvoiceError.ERR_INVOICE_INVALID_NUMBER_SEQUENCE,
                    EntityContext.INVOICE,
                    "El número de factura ya existe"
            );
        }
        return new IntegrityErrorInfo(
                new GenericErrorCatalog("ERR_DUPLICATE_ENTRY", "Registro duplicado", HttpStatus.CONFLICT, ErrorSeverity.ERROR),
                EntityContext.GENERIC,
                "Ya existe un registro con los mismos datos"
        );
    }

    private IntegrityErrorInfo parseNotNullError(String mensaje) {
        if (mensaje.contains("contrasena_hash") || mensaje.contains("password")) {
            return new IntegrityErrorInfo(
                    AuthenticationVoError.ERR_USER_PASSWORD_HASH_NULL,
                    EntityContext.USER_IDENTITY,
                    "La contraseña es obligatoria"
            );
        }
        if (mensaje.contains("correo_electronico") || mensaje.contains("email")) {
            return new IntegrityErrorInfo(
                    VoError.ERR_EMAIL_NULL,
                    EntityContext.USER_IDENTITY,
                    "El correo electrónico es obligatorio"
            );
        }
        if (mensaje.contains("nombre")) {
            return new IntegrityErrorInfo(
                    VoError.ERR_NAME_NULL,
                    EntityContext.GENERIC,
                    "El nombre es obligatorio"
            );
        }
        if (mensaje.contains("fecha_inicio") || mensaje.contains("start_date")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_START_DATE_REQUIRED", "Fecha de inicio obligatoria", HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
                    EntityContext.GENERIC,
                    "La fecha de inicio es obligatoria"
            );
        }
        if (mensaje.contains("fecha_fin") || mensaje.contains("end_date")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_END_DATE_REQUIRED", "Fecha de fin obligatoria", HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
                    EntityContext.GENERIC,
                    "La fecha de fin es obligatoria"
            );
        }
        return new IntegrityErrorInfo(
                new GenericErrorCatalog("ERR_FIELD_REQUIRED", "Campo obligatorio", HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
                EntityContext.GENERIC,
                "Hay campos obligatorios que no fueron completados"
        );
    }

    private IntegrityErrorInfo parseForeignKeyError(String mensaje) {
        if (mensaje.contains("usuario_identidad") || mensaje.contains("user_identity")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_USER_REFERENCES_EXIST", "Usuario referenciado", HttpStatus.CONFLICT, ErrorSeverity.ERROR),
                    EntityContext.USER_IDENTITY,
                    "No se puede eliminar porque tiene registros relacionados"
            );
        }
        if (mensaje.contains("rol") || mensaje.contains("role")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_ROLE_REFERENCES_EXIST", "Rol referenciado", HttpStatus.CONFLICT, ErrorSeverity.ERROR),
                    EntityContext.ROL,
                    "No se puede eliminar porque tiene asignaciones activas"
            );
        }
        if (mensaje.contains("servicio") || mensaje.contains("service")) {
            return new IntegrityErrorInfo(
                    ProvidedServiceError.ERR_SERVICE_HAS_APPOINTMENTS,
                    EntityContext.DENTAL_SERVICE,
                    "No se puede eliminar porque tiene citas programadas"
            );
        }
        if (mensaje.contains("paciente") || mensaje.contains("patient")) {
            return new IntegrityErrorInfo(
                    PatientError.ERR_PATIENT_ACTIVE_SERVICES,
                    EntityContext.PATIENT,
                    "No se puede eliminar porque tiene citas activas"
            );
        }
        if (mensaje.contains("odontologo") || mensaje.contains("dentist")) {
            return new IntegrityErrorInfo(
                    DentistError.ERR_DENTIST_ACTIVE_APPOINTMENTS,
                    EntityContext.DENTIST,
                    "No se puede eliminar porque tiene citas programadas"
            );
        }
        return new IntegrityErrorInfo(
                new GenericErrorCatalog("ERR_FOREIGN_KEY_VIOLATION", "Registro referenciado", HttpStatus.CONFLICT, ErrorSeverity.ERROR),
                EntityContext.GENERIC,
                "No se puede realizar la operación porque el registro está siendo utilizado"
        );
    }

    private IntegrityErrorInfo parseDataTooLongError(String mensaje) {
        if (mensaje.contains("nombre")) {
            return new IntegrityErrorInfo(
                    VoError.ERR_NAME_TOO_LONG,
                    EntityContext.GENERIC,
                    "El nombre excede la longitud máxima permitida"
            );
        }
        if (mensaje.contains("correo_electronico") || mensaje.contains("email")) {
            return new IntegrityErrorInfo(
                    VoError.ERR_EMAIL_LENGTH_EXCEEDED,
                    EntityContext.USER_IDENTITY,
                    "El correo electrónico excede la longitud máxima permitida"
            );
        }
        if (mensaje.contains("descripcion")) {
            return new IntegrityErrorInfo(
                    new GenericErrorCatalog("ERR_DESCRIPTION_TOO_LONG", "Descripción muy larga", HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
                    EntityContext.GENERIC,
                    "La descripción excede la longitud máxima permitida"
            );
        }
        return new IntegrityErrorInfo(
                new GenericErrorCatalog("ERR_FIELD_TOO_LONG", "Campo excede longitud", HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
                EntityContext.GENERIC,
                "El valor ingresado es demasiado largo"
        );
    }
    


    // -------------------- Mapeos para NotFoundException --------------------

    private ErrorCatalog getErrorCatalogForException(RuntimeException ex) {
        return switch (ex.getClass().getSimpleName()) {
            case "DentistNotFoundException" -> DentistError.ERR_DENTIST_NOT_FOUND;
            case "GuardianNoFoundException" -> GuardianError.ERR_GUARDIAN_NOT_FOUND;
            case "PatientNotFoundException" -> PatientError.ERR_PATIENT_NOT_FOUND;
            case "ReceptionistNotFoundException" -> ReceptionistError.ERR_RECEPTIONIST_NOT_FOUND;
            case "AdministrativeReportNotFoundException" -> AdministrativeReportError.ERR_REPORT_NOT_FOUND;
            case "CompanyNotFoundException" -> CompanyError.ERR_COMPANY_NOT_FOUND;
            case "ContractNotFoundException" -> ContractError.ERR_CONTRACT_NOT_FOUND;
            case "ExpenseNotFoundException" -> new GenericErrorCatalog("ERR_EXPENSE_NOT_FOUND", "Gasto no encontrado", HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);
            case "JournalEntryNotFoundException" -> JournalEntryError.ERR_JOURNALENTRY_NOT_FOUND;
            case "LedgerAccountNotFoundException" -> LedgerAccountError.ERR_ACCOUNT_NOT_FOUND;
            case "OpeningBalanceNotFoundException" -> OpeningBalanceError.ERR_OPENING_BALANCE_NOT_FOUND;
            case "ThirdPartyNotFoundException" -> ThirdPartiesError.ERR_THIRD_PARTY_NOT_FOUND;
            case "RolNotFoundException" -> RolError.ERR_ROL_NOT_FOUND;
            case "UserRolAssignmentNotFoundException" -> UserRolAssignmentError.ERR_ASSIGNMENT_NOT_FOUND;
            case "ShiftNotFoundException" -> ShiftError.ERR_SHIFT_NOT_FOUND;
            case "UserIdentityNoFoundException" -> UserIdentityError.ERR_USER_NOT_FOUND;
            case "InvoiceNotFoundException" -> InvoiceError.ERR_INVOICE_NOT_FOUND;
            case "RateNotFoundException" -> RateError.ERR_RATE_NOT_FOUND;
            case "TreatmentNotFoundException" -> TreatmentError.ERR_TREATMENT_NOT_FOUND;
            case "ProvidedServiceNotFoundException" -> ProvidedServiceError.ERR_SERVICE_NOT_FOUND;
            case "AppointmentNotFoundException" -> AppointmentError.ERR_APPOINTMENT_NOT_FOUND;
            default -> new GenericErrorCatalog("ERR_RESOURCE_NOT_FOUND", "Recurso no encontrado", HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);
        };
    }

    private DomainContext getContextForException(RuntimeException ex) {
        return switch (ex.getClass().getSimpleName()) {
            case "DentistNotFoundException" -> EntityContext.DENTIST;
            case "GuardianNoFoundException" -> EntityContext.GUARDIAN;
            case "PatientNotFoundException" -> EntityContext.PATIENT;
            case "ReceptionistNotFoundException" -> EntityContext.RECEPTIONIST;
            case "AdministrativeReportNotFoundException" -> EntityContext.ADMINISTRATIVEREPORT;
            case "CompanyNotFoundException" -> EntityContext.COMPANY;
            case "ContractNotFoundException" -> EntityContext.CONTRACT;
            case "JournalEntryNotFoundException" -> EntityContext.JOURNALENTRY;
            case "LedgerAccountNotFoundException" -> EntityContext.LEDGERACCOUNT;
            case "OpeningBalanceNotFoundException" -> EntityContext.OPENINGBALANCE;
            case "ThirdPartyNotFoundException" -> EntityContext.THISPARTIES;
            case "RolNotFoundException" -> EntityContext.ROL;
            case "UserRolAssignmentNotFoundException" -> EntityContext.ASSIGNMENT;
            case "ShiftNotFoundException" -> EntityContext.SHIFT;
            case "UserIdentityNoFoundException" -> EntityContext.USER_IDENTITY;
            case "InvoiceNotFoundException" -> EntityContext.INVOICE;
            case "RateNotFoundException" -> EntityContext.RATE;
            case "TreatmentNotFoundException" -> EntityContext.TREATMENT;
            case "ProvidedServiceNotFoundException" -> EntityContext.DENTAL_SERVICE;
            case "AppointmentNotFoundException" -> EntityContext.APPOINTMENT;
            default -> EntityContext.GENERIC;
        };
    }
}