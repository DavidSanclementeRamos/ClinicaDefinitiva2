package com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentId;

public enum ScheduleVOError implements ErrorCatalog {

    ERR_APPOINTMENT_ID_REQUIRED(
            "RN-APPT-01",
            "error.appointment.idRequired",
            "El valor de AppointmentId no puede ser nulo"
    ),

    ERR_APPOINTMENT_ID_EMPTY(
            "RN-APPT-02",
            "error.appointment.idEmpty",
            "El valor de AppointmentId no puede estar vacío"
    ),

    ERR_APPOINTMENT_STATUS_REQUIRED(
            "RN-APPT-01",
            "error.appointment.statusRequired",
            "El estado de Appointment no puede ser nulo"
    ),

    ERR_APPOINTMENT_STATUS_INVALID_TRANSITION(
            "RN-APPT-02",
            "error.appointment.statusInvalidTransition",
            "No se puede transicionar desde el estado actual a un estado inválido"
    ),

    ERR_AVAIL_ID_REQUIRED(
            "RN-AVAIL-01",
            "error.availability.idRequired",
            "El valor de AvailabilityId no puede ser nulo"
    ),

    ERR_AVAIL_ID_BLANK(
            "RN-AVAIL-02",
            "error.availability.idBlank",
            "El valor de AvailabilityId no puede estar vacío"
    ),

    ERR_AVAIL_STATUS_REQUIRED(
            "RN-AVAIL-01",
            "error.availability.statusRequired",
            "El estado de Availability no puede ser nulo"
    ),

    ERR_SHIFT_ID_REQUIRED(
            "RN-SHIFT-01",
            "error.shift.idRequired",
            "El valor de ShiftId no puede ser nulo"
    ),

    ERR_SHIFT_ID_BLANK(
            "RN-SHIFT-02",
            "error.shift.idBlank",
            "El valor de ShiftId no puede estar vacío"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ScheduleVOError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
