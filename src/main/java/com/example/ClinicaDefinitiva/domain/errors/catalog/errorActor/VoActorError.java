package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VoActorError implements ErrorCatalog {
   ERR_ID_NULL(
        "RN-ACTOR-VO-001",
        "error.id.null",
        "El identificador no puede ser nulo"
),

ERR_ID_INVALID_FORMAT(
        "RN-ACTOR-VO-002",
        "error.id.format",
        "El identificador no tiene un formato válido"
),

ERR_WORKING_HOURS_NULL(
        "RN-ACTOR-VO-003",
        "error.workinghours.null",
        "Los componentes del horario laboral no pueden ser nulos"
),

ERR_WORKING_HOURS_INVALID_RANGE(
        "RN-ACTOR-VO-004",
        "error.workinghours.range",
        "La hora de inicio debe ser anterior a la hora de fin"
),

ERR_WORKING_HOURS_INVALID_DECLARED(
        "RN-ACTOR-VO-005",
        "error.workinghours.declared.invalid",
        "Las horas semanales declaradas deben ser positivas"
),

ERR_WORKING_HOURS_EXCEEDS_LEGAL_LIMIT(
        "RN-ACTOR-VO-006",
        "error.workinghours.declared.exceeds",
        "Las horas semanales declaradas exceden el límite legal"
),

ERR_TYPE_GUARDIAN_CODE_NULL(
        "RN-ACTOR-VO-007",
        "error.typeguardian.code.null",
        "El código del tipo de responsable no puede ser nulo"
),

ERR_TYPE_GUARDIAN_CODE_BLANK(
        "RN-ACTOR-VO-008",
        "error.typeguardian.code.blank",
        "El código del tipo de responsable no puede estar vacío"
),

ERR_TYPE_GUARDIAN_CODE_INVALID(
        "RN-ACTOR-VO-009",
        "error.typeguardian.code.invalid",
        "El código del tipo de responsable no es válido"
),

ERR_TYPE_GUARDIAN_DESCRIPTION_BLANK(
        "RN-ACTOR-VO-010",
        "error.typeguardian.description.blank",
        "La descripción del tipo de responsable no puede estar vacía"
),

ERR_SECTOR_NULL(
        "RN-ACTOR-VO-011",
        "error.sector.null",
        "El sector no puede ser nulo"
),

ERR_SECTOR_BLANK(
        "RN-ACTOR-VO-012",
        "error.sector.blank",
        "El sector no puede estar vacío"
),

ERR_SECTOR_NOT_ALLOWED(
        "RN-ACTOR-VO-013",
        "error.sector.notallowed",
        "El sector especificado no está permitido"
),

ERR_FULLNAME_NULL(
        "RN-ACTOR-VO-014",
        "error.fullname.null",
        "El nombre completo no puede ser nulo"
),

ERR_FULLNAME_BLANK(
        "RN-ACTOR-VO-015",
        "error.fullname.blank",
        "El nombre y apellido no pueden estar vacíos"
),

ERR_BIRTHDATE_NULL(
        "RN-ACTOR-VO-016",
        "error.birthdate.null",
        "La fecha de nacimiento no puede ser nula"
),

ERR_BIRTHDATE_FUTURE(
        "RN-ACTOR-VO-017",
        "error.birthdate.future",
        "La fecha de nacimiento no puede ser futura"
),

ERR_BIRTHDATE_INVALID_RANGE(
        "RN-ACTOR-VO-018",
        "error.birthdate.range",
        "La fecha de nacimiento excede el rango válido (edad > 130 años)"
),

ERR_AGE_OUT_OF_RANGE(
        "RN-ACTOR-VO-019",
        "error.age.range",
        "La edad está fuera del rango válido (0-130 años)"
),

ERR_BLOODTYPE_INVALID(
        "RN-ACTOR-VO-020",
        "error.bloodtype.invalid",
        "El tipo de sangre especificado no es válido"
),

ERR_DOCUMENT_NULL(
        "RN-ACTOR-VO-021",
        "error.document.null",
        "El documento no puede ser nulo"
),

ERR_DOCUMENT_BLANK(
        "RN-ACTOR-VO-022",
        "error.document.blank",
        "El documento no puede estar vacío"
),

ERR_DOCUMENT_INVALID_FORMAT(
        "RN-ACTOR-VO-023",
        "error.document.invalidFormat",
        "El formato del documento es inválido"
),

ERR_DENTIST_INVALID_SPECIALTY(
        "RN-ACTOR-VO-024",
        "error.dentist.invalidSpecialty",
        "La especialidad del dentista especificada no es válida"
),

ERR_DENTIST_STATUS_NULL(
        "RN-ACTOR-VO-025",
        "error.dentist.status.null",
        "El estado del dentista no puede ser nulo"
);
    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    VoActorError(String code, String messageKey, String defaultMessage) {
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
