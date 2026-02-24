package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VoActorError implements ErrorCatalog {
    ERR_AVAILABILITY_STATUS_NULL(
            "RN-VO-STATUS-001",
            "error.availability.status.null",
            "El estado de disponibilidad no puede ser nulo"
    ),
    ERR_AVAILABILITY_STATUS_TRANSITION_NULL(
            "RN-VO-STATUS-002",
            "error.availability.transition.null",
            "El estado de transición no puede ser nulo"
    ),
    ERR_AVAILABILITY_STATUS_INVALID_TRANSITION(
            "RN-VO-STATUS-003",
            "error.availability.transition.invalid",
            "La transición de estado no es válida"
    ),
    ERR_ID_NULL(
            "RN-VO-ID-001",
            "error.id.null",
            "El identificador no puede ser nulo"
    ),
    ERR_ID_BLANK(
            "RN-VO-ID-002",
            "error.id.blank",
            "El identificador no puede estar vacío"
    ),
    ERR_ID_INVALID_FORMAT(
            "RN-VO-ID-003",
            "error.id.format",
            "El identificador no tiene un formato válido"
    ),

    ERR_WORKING_HOURS_NULL(
            "RN-VO-HOURS-001",
            "error.workinghours.null",
            "Los componentes del horario laboral no pueden ser nulos"
    ),
    ERR_WORKING_HOURS_INVALID_RANGE(
            "RN-VO-HOURS-002",
            "error.workinghours.range",
            "La hora de inicio debe ser anterior a la hora de fin"
    ),
    ERR_WORKING_HOURS_INVALID_DECLARED(
            "RN-VO-HOURS-003",
            "error.workinghours.declared.invalid",
            "Las horas semanales declaradas deben ser positivas"
    ),
    ERR_WORKING_HOURS_EXCEEDS_LEGAL_LIMIT(
            "RN-VO-HOURS-004",
            "error.workinghours.declared.exceeds",
            "Las horas semanales declaradas exceden el límite legal"
    ),

    ERR_TYPE_GUARDIAN_CODE_NULL(
            "RN-VO-GUARDIAN-001",
            "error.typeguardian.code.null",
            "El código del tipo de responsable no puede ser nulo"
    ),
    ERR_TYPE_GUARDIAN_CODE_BLANK(
            "RN-VO-GUARDIAN-002",
            "error.typeguardian.code.blank",
            "El código del tipo de responsable no puede estar vacío"
    ),
    ERR_TYPE_GUARDIAN_CODE_INVALID(
            "RN-VO-GUARDIAN-003",
            "error.typeguardian.code.invalid",
            "El código del tipo de responsable no es válido"
    ),
    ERR_TYPE_GUARDIAN_DESCRIPTION_BLANK(
            "RN-VO-GUARDIAN-004",
            "error.typeguardian.description.blank",
            "La descripción del tipo de responsable no puede estar vacía"
    ),


    // En ValueObjectError enum
    ERR_SECTOR_NULL(
            "RN-VO-SECTOR-001",
            "error.sector.null",
            "El sector no puede ser nulo"
    ),
    ERR_SECTOR_BLANK(
            "RN-VO-SECTOR-002",
            "error.sector.blank",
            "El sector no puede estar vacío"
    ),
    ERR_SECTOR_NOT_ALLOWED(
            "RN-VO-SECTOR-003",
            "error.sector.notallowed",
            "El sector especificado no está permitido"
    ),

    // FullName errors
    ERR_FULLNAME_NULL(
            "RN-VO-001",
            "error.fullname.null",
            "El nombre completo no puede ser nulo"
    ),
    ERR_FULLNAME_BLANK(
            "RN-VO-002",
            "error.fullname.blank",
            "El nombre y apellido no pueden estar vacíos"
    ),

    // DateOfBirth errors
    ERR_BIRTHDATE_NULL(
            "RN-VO-003",
            "error.birthdate.null",
            "La fecha de nacimiento no puede ser nula"
    ),
    ERR_BIRTHDATE_FUTURE(
            "RN-VO-004",
            "error.birthdate.future",
            "La fecha de nacimiento no puede ser futura"
    ),
    ERR_BIRTHDATE_INVALID_RANGE(
            "RN-VO-005",
            "error.birthdate.range",
            "La fecha de nacimiento excede el rango válido (edad > 130 años)"
    ),

    // Age errors
    ERR_AGE_OUT_OF_RANGE(
            "RN-VO-006",
            "error.age.range",
            "La edad está fuera del rango válido (0-130 años)"
    ),

    // PhoneNumber errors
    ERR_PHONE_NULL(
            "RN-VO-007",
            "error.phone.null",
            "El número telefónico no puede ser nulo"
    ),
    ERR_PHONE_BLANK(
            "RN-VO-008",
            "error.phone.blank",
            "El número telefónico no puede estar vacío"
    ),
    ERR_PHONE_INVALID_FORMAT(
            "RN-VO-009",
            "error.phone.format",
            "El formato del número telefónico es inválido"
    ),

    // Address errors
    ERR_ADDRESS_NULL(
            "RN-VO-010",
            "error.address.null",
            "Los campos de dirección no pueden ser nulos"
    ),
    ERR_ADDRESS_BLANK(
            "RN-VO-011",
            "error.address.blank",
            "Los campos de dirección no pueden estar vacíos"
    ), ERR_BLOODTYPE_INVALID("","" ,"" ),

    ERR_DOCUMENT_NULL("", "", ""),
    ERR_DOCUMENT_BLANK("", "", ""),
    ERR_DOCUMENT_INVALID_FORMAT("","" ,"" ),
    ERR_DENTIST_INVALID_SPECIALTY("", "", ""),
    ERR_DENTIST_STATUS_NULL("","","");

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
