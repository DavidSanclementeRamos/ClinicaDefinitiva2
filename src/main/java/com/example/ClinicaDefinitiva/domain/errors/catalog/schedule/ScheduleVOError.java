package com.example.ClinicaDefinitiva.domain.errors.catalog.schedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

// CORREGIDO: prefijo RN-APPT- → RN-APPT-VO- para eliminar colisión con AppointmentError
// CORREGIDO: ERR_APPT_INCOMPLETE_COMPLETION tenía código duplicado RN-APPT-003, ahora RN-APPT-VO-004
public enum ScheduleVOError implements ErrorCatalog {

    ERR_APPOINTMENT_ID_REQUIRED("RN-APPT-VO-001","error.appointment.idRequired","El valor de AppointmentId no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_APPOINTMENT_STATUS_REQUIRED("RN-APPT-VO-002","error.appointment.statusRequired","El estado de Appointment no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_APPOINTMENT_STATUS_INVALID_TRANSITION("RN-APPT-VO-003","error.appointment.statusInvalidTransition","No se puede transicionar desde el estado actual a un estado inválido",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    // CORREGIDO: código duplicado RN-APPT-003 → RN-APPT-VO-004
    ERR_APPT_INCOMPLETE_COMPLETION("RN-APPT-VO-004","error.appointment.incompleteCompletion","Solo puede finalizarse si tiene duración real y notas clínicas",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ScheduleVOError(String code, String messageKey, String defaultMessage,
                    HttpStatus suggestedHttpStatus, ErrorSeverity severity) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
        this.suggestedHttpStatus = suggestedHttpStatus;
        this.severity = severity;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }
    @Override public HttpStatus getSuggestedHttpStatus() { return suggestedHttpStatus; }
    @Override public ErrorSeverity getSeverity() { return severity; }
}