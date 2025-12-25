package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
/**
 CATÁLOGOS DE ERROR CONSOLIDADOS - MÓDULO ACTOR v1.0
 Convención de numeración:
 - Los números preservan trazabilidad histórica
 - Las reglas eliminadas se documentan con comentarios
 - Los nuevos errores continúan la numeración secuencial
**/
public enum DentistError implements ErrorCatalog {

    // ========== APLICADAS ==========
    ERR_DENTIST_AGE_INSUFFICIENT(
            "RN-DENTIST-001",
            "error.dentist.age",
            "El odontólogo debe tener al menos 25 años"
    ),
    ERR_DENTIST_MISSING_AVAILABILITY(
            "RN-DENTIST-002",
            "error.dentist.availability.missing",
            "El odontólogo debe registrar disponibilidad inicial (mínimo 40 horas semanales)"
    ),
    ERR_DENTIST_ACTIVE_APPOINTMENTS(
            "RN-DENTIST-003",
            "error.dentist.deactivate.appointments",
            "No puede desactivarse si tiene citas activas en las próximas 24 horas"
    ),
    ERR_DENTIST_TIME_CONFLICT(
            "RN-DENTIST-004",
            "error.dentist.schedule.conflict",
            "El odontólogo ya tiene una cita agendada en este horario"
    ),
    ERR_DENTIST_NOT_AVAILABLE(
            "RN-DENTIST-005",
            "error.dentist.not.available",
            "El odontólogo no está disponible para agendar en este momento"
    ),

    // RN-DENTIST-006: ELIMINADA (2024-12)
    // Motivo: Delegada a UserAccessError.ERR_USER_INACTIVE
    // Original: "Solo puede editarse si está activo"

    ERR_DENTIST_INVALID_SPECIALTY(
            "RN-DENTIST-007",
            "error.dentist.specialty.invalid",
            "La especialidad proporcionada no es reconocida"
    ),

    // RN-DENTIST-008: ELIMINADA (2024-12)
    // Motivo: Duplicada con RN-DENTIST-006
    // Original: "No puede crearse con estado INACTIVO"

    // RN-DENTIST-009: ELIMINADA (2024-12)
    // Motivo: Dividida en catálogos específicos de ValueObject
    // Original: "Debe tener nombre y documento válidos"
    // Reemplazo: ValueObjectError.ERR_FULLNAME_BLANK, ERR_DOCUMENT_INVALID_FORMAT

    ERR_DENTIST_EMPTY_AVAILABILITY(
            "RN-DENTIST-010",
            "error.dentist.availability.empty",
            "La disponibilidad del odontólogo no puede quedar vacía"
    ),

    // ========== NUEVOS ==========
    ERR_DENTIST_OUT_OF_WORKING_HOURS(
            "RN-DENTIST-011",
            "error.dentist.working.hours",
            "El horario solicitado está fuera de las horas laborales declaradas"
    ),
    ERR_DENTIST_INVALID_VACATION_RANGE(
            "RN-DENTIST-012",
            "error.dentist.vacation.range",
            "El rango de vacaciones solicitado es inválido"
    ),
    ERR_DENTIST_VACATION_CONFLICT(
            "RN-DENTIST-013",
            "error.dentist.vacation.conflict",
            "Hay citas agendadas que entran en conflicto con el período de vacaciones"
    ),
    ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS(
            "RN-DENTIST-014",
            "error.dentist.reschedule.hours",
            "La reagendación está fuera de las horas laborales del odontólogo"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    DentistError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() { return code; }
    @Override
    public String getMessageKey() { return messageKey; }
    @Override
    public String getDefaultMessage() { return defaultMessage; }
}
