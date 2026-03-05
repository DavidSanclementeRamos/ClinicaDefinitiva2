package com.example.ClinicaDefinitiva.domain.errors.catalog.schedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ScheduleVOError implements ErrorCatalog {
    ERR_APPOINTMENT_ID_REQUIRED(
            "RN-APPT-001",
            "error.appointment.idRequired",
            "El valor de AppointmentId no puede ser nulo"
    ),

    

    ERR_APPOINTMENT_STATUS_REQUIRED(
            "RN-APPT-002",
            "error.appointment.statusRequired",
            "El estado de Appointment no puede ser nulo"
    ),

    ERR_APPOINTMENT_STATUS_INVALID_TRANSITION(
            "RN-APPT-003",
            "error.appointment.statusInvalidTransition",
            "No se puede transicionar desde el estado actual a un estado inválido"
    ),
    
    ERR_APPT_INCOMPLETE_COMPLETION(
            "RN-APPT-003",
            "error.appointment.incompleteCompletion",
            "Solo puede finalizarse si tiene duración real y notas clínicas"
    ),



    

   
;
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
