package com.example.ClinicaDefinitiva.domain.errors;

public enum ErrorCatalog {

   /** METHOD_NOT_ALLOWED("ERR_HTTP_405", "Método HTTP no permitido."),
    MISSING_REQUEST_PARAM("ERR_PARAM_001", "Parámetro de solicitud faltante."),
    INVALID_JSON("ERR_JSON_001", "Cuerpo JSON mal formado."),
    INVALID_PARAMETERS("ERR_PARAM_001", "Parámetros inválidos en ruta o query."),
    TYPE_MISMATCH("ERR_TYPE_400", "Parámetro de tipo incorrecto."),
    UNSUPPORTED_MEDIA_TYPE("ERR_CONT_415", "Tipo de contenido no soportado."),
    ROUTE_NOT_FOUND("ERR_ROUTE_404", "Recurso no encontrado."),
    ACCESS_DENIED("ERR_AUTH_403", "Acceso denegado."),
    DATA_INTEGRITY_VIOLATION("ERR_DB_409", "Conflicto de integridad de datos."),
    GENERIC_ERROR("ERR_GEN_001", "Error inesperado."),
    ,**/
// En ValueObjectError enum
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
    ),

    // DocumentId errors (nuevo)
    ERR_DOCUMENT_NULL(
            "RN-VO-012",
            "error.document.null",
            "El documento de identidad no puede ser nulo"
    ),
    ERR_DOCUMENT_INVALID_FORMAT(
            "RN-VO-013",
            "error.document.format",
            "El formato del documento de identidad es inválido"
    ),



    ERR_DENTIST_AGE_INSUFFICIENT(
           "RN-DENTIST-001",
           "error.dentist.ageInsufficient",
           "Un odontólogo debe tener al menos 25 años al crearse"
   ),
    ERR_DOCUMENT_BLANK("","",""),

    ERR_DENTIST_MISSING_AVAILABILITY(
            "RN-DENTIST-002",
            "error.dentist.missingAvailability",
            "Debe registrar disponibilidad inicial"
    ),

    ERR_DENTIST_ACTIVE_APPOINTMENTS(
            "RN-DENTIST-003",
            "error.dentist.activeAppointments",
            "No puede desactivarse si tiene citas activas en las próximas 24 horas"
    ),

    ERR_DENTIST_TIME_CONFLICT(
            "RN-DENTIST-004",
            "error.dentist.timeConflict",
            "No puede tener dos citas en el mismo horario"
    ),

    ERR_DENTIST_NOT_AVAILABLE(
            "RN-DENTIST-005",
            "error.dentist.notAvailable",
            "Solo puede agendar si está activo y tiene disponibilidad"
    ),

    ERR_DENTIST_NOT_EDITABLE(// ELIMINAR
            "RN-DENTIST-006",
            "error.dentist.notEditable",
            "Solo puede editarse si está activo"
    ),

    ERR_DENTIST_INVALID_SPECIALTY(
            "RN-DENTIST-007",
            "error.dentist.invalidSpecialty",
            "Debe tener especialidad reconocida válida"
    ),

    ERR_DENTIST_INVALID_INITIAL_STATUS(// ELIMINAR
            "RN-DENTIST-008",
            "error.dentist.invalidInitialStatus",
            "No puede crearse con estado INACTIVO"
    ),

    ERR_DENTIST_MISSING_REQUIRED_FIELDS(// ELIMINAR
            "RN-DENTIST-009",
            "error.dentist.missingRequiredFields",
            "Debe tener nombre y documento válidos"
    ),

    ERR_DENTIST_EMPTY_AVAILABILITY(
            "RN-DENTIST-010",
            "error.dentist.emptyAvailability",
            "La disponibilidad no puede quedar vacía al editar"
    ),
    ERR_DENTIST_OUT_OF_WORKING_HOURS(
            "RN-DENTIST-011",
            "error.dentist.outOfWorkingHours",
            "El horario solicitado está fuera de la jornada laboral del odontólogo"
    ),
    ERR_DENTIST_INVALID_VACATION_RANGE(
            "RN-DENTIST-012",
            "error.dentist.invalidVacationRange",
            "El rango de fechas de vacaciones es inválido"
    ),
    ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS(
            "RN-DENTIST-013",
            "error.dentist.rescheduleOutOfWorkingHours",
            "La nueva fecha no está dentro del horario laboral del odontólogo"
    ),

    ERR_DENTIST_VACATION_CONFLICT(
            "RN-DENTIST-014",
            "error.dentist.vacationConflict",
            "No se puede tomar vacaciones: existen citas en conflicto"
    ),


    ERR_BLOODTYPE_INVALID(
            "RN-BLOODTYPE-001", "error.bloodtype.invalid",
            "Tipo de sangre inválido"),

    ERR_RECEPTIONIST_CREATION_REQUIRES_ACTIVE_USER(
            "RN-SECRETARY-001",
            "error.secretary.creationRequiresActiveUser",
            "No se puede crear secretari@ con un usuario inactivo"
    ),

    ERR_RECEPTIONIST_NOT_EDITABLE("RN-RECEPTIONIST-002",
            "error.receptionist.notEditable",
            "Solo puede editarse si está activo"),


    ERR_RECEPTIONIST_DENTIST_INACTIVE(
            "RN-RECEPTIONIST-001",
            "error.receptionist.dentistInactive",
            "El dentista asociado se encuentra inactivo"
    ),

    ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT(
            "RN-RECEPTIONIST-002",
            "error.receptionist.duplicateAppointment",
            "La cita ya existe y no puede duplicarse"
    ),

    ERR_RECEPTIONIST_LATE_CANCELLATION(
            "RN-RECEPTIONIST-003",
            "error.receptionist.lateCancellation",
            "La cancelación se realizó fuera del tiempo permitido"
    ),
 EER_RECEPTIONIST_INACTIVATION_REQUIRES_REASON("","",
         ""),

    ERR_USER_INACTIVE(
            "RN-USER-001",
            "error.user.inactive",
            "No se puede realizar la operación porque el usuario está inactivo"
    ),

    // citas
   PENDING_APPOINTMENT("APP01", "error.patient.pendingAppointment",
           "Tiene citas pendientes en las próximas {hours} horas"),
    FUTURE_APPOINTMENT("APP02", "error.patient.futureAppointment",
            "No se puede desactivar: tiene citas futuras"),
    INVALID_DATE_RANGE_APPOINTMENT("ERR_03", "error.appointment.invalidDateRange",
            "La fecha de inicio no puede ser anterior al final"),
    NULL_START_DATE_APPOINTMENT("ERR_04", "error.appointment.nullStartDate",
            "La fecha de inicio no puede ser NULL"),
    NULL_END_DATE_APPOINTMENT("ERR_05", "error.appointment.nullEndDate",
            "La fecha final no puede ser NULL"),
    OUTSIDE_AVAILABILITY_APPOINTMENT("ERR_06", "error.appointment.outsideAvailability",
            "El intervalo ya está cubierto por la disponibilidad declarada"),
    TIME_NO_AVAILABILITY_APPOINTMENT("ERR_07", "error.appointment.timeNotAvailable",
            "El intervalo ya está ocupado por otra cita"),



    //  Errores de asientos contables
    ERR_REPORT_DUPLICATE_JOURNAL_ENTRY("RN-ADMINREPORT-009", "error.report.duplicateJournalEntry",
            "No puede agregarse referencia duplicada a asiento contable"),

    ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND("RN-ADMINREPORT-010", "error.report.journalEntryNotFound",
            "El asiento contable no está referenciado en el reporte"),


    //  Errores de indicadores
    ERR_REPORT_INDICATOR_NULL("RN-ADMINREPORT-XXX", "error.report.indicatorNull",
            "El indicador no puede ser nulo"),

    ERR_REPORT_INDICATOR_NOT_FOUND("RN-ADMINREPORT-XXX", "error.report.indicatorNotFound",
            "El indicador no existe en el reporte"),


    //  Errores de documentos adjuntos
    ERR_REPORT_ATTACHMENT_NULL("RN-ADMINREPORT-XXX", "error.report.attachmentNull",
            "El documento no puede ser nulo"),

    ERR_REPORT_ATTACHMENT_NOT_FOUND("RN-ADMINREPORT-XXX", "error.report.attachmentNotFound",
            "El documento no existe en el reporte"),


    //  Errores de estado de reporte
    ERR_REPORT_NOT_EDITABLE("RN-ADMINREPORT-001", "error.report.notEditable",
            "Solo puede editarse si está en estado DRAFT"),

    ERR_REPORT_INCOMPLETE("RN-ADMINREPORT-002", "error.report.incomplete",
            "El reporte debe tener al menos un asiento contable o un indicador"),

    ERR_REPORT_CANNOT_SUBMIT("RN-ADMINREPORT-003", "error.report.cannotSubmit",
            "Solo puede enviarse a revisión desde DRAFT"),

    ERR_REPORT_CANNOT_APPROVE("RN-ADMINREPORT-004", "error.report.cannotApprove",
            "Solo puede aprobarse si está en revisión"),

    ERR_REPORT_CANNOT_REJECT("RN-ADMINREPORT-005", "error.report.cannotReject",
            "Solo puede rechazarse si está en revisión"),

    ERR_REPORT_REJECTION_REQUIRES_REASON("RN-ADMINREPORT-006", "error.report.rejectionRequiresReason",
            "Se requiere una razón para rechazar el reporte"),

    ERR_REPORT_CANNOT_ARCHIVE("RN-ADMINREPORT-007", "error.report.cannotArchive",
            "Solo puede archivarse si está publicado"),

    ERR_REPORT_CANNOT_UNARCHIVE("RN-ADMINREPORT-008", "error.report.cannotUnarchive",
            "Solo puede desarchivarse si está archivado"),

    ERR_REPORT_MISSING_APPROVER("RN-ADMINREPORT-011", "error.report.missingApprover",
            "La aprobación requiere usuario aprobador válido"),

    // --- Company (Empresa) ---
    ERR_COMPANY_MISSING_TAX_ID("RN-COMPANY-001", "error.company.missingTaxId",
            "La empresa debe tener NIT único y válido"),

    ERR_COMPANY_FUTURE_INCORPORATION_DATE("RN-COMPANY-002", "error.company.futureIncorporationDate",
            "La fecha de constitución no puede ser futura"),

    ERR_COMPANY_NOT_EDITABLE("RN-COMPANY-003", "error.company.notEditable",
            "La empresa solo puede editarse si está en estado ACTIVE o SUSPENDED"),

    ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY("RN-COMPANY-004", "error.company.cannotReactivateDirectly",
            "Una empresa inactiva no puede reactivarse sin proceso formal"),

    ERR_COMPANY_MISSING_PERSON_TYPE("RN-COMPANY-005", "error.company.missingPersonType",
            "El tipo de persona es obligatorio"),

    ERR_COMPANY_CANNOT_MODIFY_TAX_ID("RN-COMPANY-006", "error.company.cannotModifyTaxId",
            "El NIT no puede modificarse una vez registrado"),

    ERR_COMPANY_MISSING_INCORPORATION_DATE("RN-COMPANY-007", "error.company.missingIncorporationDate",
            "La fecha de constitución es obligatoria"),

    ERR_COMPANY_MISSING_CONTACT("RN-COMPANY-008", "error.company.missingContact",
            "Debe registrarse al menos un medio de contacto válido (email o teléfono)"),

    ERR_COMPANY_INVALID_INCORPORATION_DATE("RN-COMPANY-009", "error.company.invalidIncorporationDate",
            "La fecha de constitución no es válida (no puede ser anterior a 1800)"),

    // --- Contract ---
// Catálogo de errores poblado según ADR-Descubrimiento de Reglas de Negocio por Agregado (Contract)

    ERR_CONTRACT_INVALID_DATES("RN-CONTRACT-001", "error.contract.invalidDates",
            "La fecha de fin debe ser posterior a la fecha de inicio"),

    ERR_CONTRACT_NOT_EDITABLE("RN-CONTRACT-002", "error.contract.notEditable",
            "Solo puede editarse si está en estado ACTIVE y no vencido"),

    ERR_CONTRACT_CANNOT_SUSPEND("RN-CONTRACT-003", "error.contract.cannotSuspend",
            "Solo puede suspenderse si está en estado ACTIVE"),

    ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE("RN-CONTRACT-004", "error.contract.expiredCannotReactivate",
            "No puede reactivarse si está vencido"),

    ERR_CONTRACT_INVALID_EXTENSION("RN-CONTRACT-005", "error.contract.invalidExtension",
            "La extensión de vigencia solo permite fechas posteriores"),

    ERR_CONTRACT_MISSING_COVERAGE_TYPE("RN-CONTRACT-006", "error.contract.missingCoverageType",
            "Debe tener tipo de cobertura válido"),

// RN-CONTRACT-007: Evento de sistema (ContractNearExpirationEvent) — no genera error de catálogo.

    ERR_CONTRACT_TERMINATION_REQUIRES_REASON("RN-CONTRACT-008", "error.contract.terminationRequiresReason",
            "La terminación requiere motivo obligatorio"),
    // --- Contract (Contrato) ---
    ERR_CONTRACT_MISSING_NEW_END_DATE("RN-CONTRACT-009", "error.contract.missingNewEndDate",
            "La nueva fecha de fin es obligatoria"),
            // --- Contract (Contrato) ---
            ERR_CONTRACT_NEW_END_DATE_IN_PAST("RN-CONTRACT-010", "error.contract.newEndDateInPast",
                    "La nueva fecha de fin no puede estar en el pasado"),
    // --- Contract (Contrato) ---
    ERR_CONTRACT_CANNOT_REACTIVATE("RN-CONTRACT-011", "error.contract.cannotReactivate",
            "Solo se pueden reactivar contratos suspendidos"),
    // --- Contract (Contrato) ---
    ERR_CONTRACT_ALREADY_TERMINATED("RN-CONTRACT-012", "error.contract.alreadyTerminated",
            "El contrato ya está terminado"),
    // --- Contract (Contrato) ---
    ERR_CONTRACT_MISSING_START_DATE("RN-CONTRACT-014", "error.contract.missingStartDate",
            "La fecha de inicio es obligatoria"),

    ERR_CONTRACT_MISSING_END_DATE("RN-CONTRACT-015", "error.contract.missingEndDate",
            "La fecha de fin es obligatoria"),
    // --- Contract (Contrato) ---
    ERR_CONTRACT_EXPIRED_NOT_EDITABLE("RN-CONTRACT-013", "error.contract.expiredNotEditable",
            "No se puede editar un contrato vencido"),


    // --- JournalEntry ---
// Catálogo de errores poblado según ADR-Descubrimiento de Reglas de Negocio por Agregado (JournalEntry)

    ERR_JOURNALENTRY_MISSING_ACCOUNT("RN-JOURNALENTRY-001", "error.journalEntry.missingAccount",
                                             "Debe especificarse una cuenta contable válida"),

    ERR_JOURNALENTRY_INVALID_AMOUNT("RN-JOURNALENTRY-002", "error.journalEntry.invalidAmount",
                                            "El monto debe ser mayor a cero"),

    ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH("RN-JOURNALENTRY-003", "error.journalEntry.debitCreditMismatch",
                                                   "Los débitos y créditos deben estar balanceados"),

    ERR_JOURNALENTRY_DUPLICATE_REFERENCE("RN-JOURNALENTRY-004", "error.journalEntry.duplicateReference",
                                                 "La referencia del asiento contable ya existe"),

    ERR_JOURNALENTRY_DATE_IN_FUTURE("RN-JOURNALENTRY-005", "error.journalEntry.dateInFuture",
                                            "La fecha del asiento no puede estar en el futuro"),

    ERR_JOURNALENTRY_DATE_BEFORE_PERIOD("RN-JOURNALENTRY-006", "error.journalEntry.dateBeforePeriod",
                                                "La fecha del asiento no puede ser anterior al inicio del período contable"),

    ERR_JOURNALENTRY_MISSING_DESCRIPTION("RN-JOURNALENTRY-007", "error.journalEntry.missingDescription",
                                                 "La descripción del asiento es obligatoria"),

    ERR_JOURNALENTRY_NOT_EDITABLE("RN-JOURNALENTRY-008", "error.journalEntry.notEditable",
                                          "El asiento no puede editarse una vez publicado"),

    ERR_JOURNALENTRY_CANNOT_DELETE("RN-JOURNALENTRY-009", "error.journalEntry.cannotDelete",
                                           "El asiento no puede eliminarse si está conciliado"),

    ERR_JOURNALENTRY_UNAUTHORIZED_USER("RN-JOURNALENTRY-010", "error.journalEntry.unauthorizedUser",
                                               "El usuario no tiene permisos para registrar asientos contables"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_LINE_NOT_FOUND("RN-JOURNALENTRY-011", "error.journalEntry.lineNotFound",
            "La línea no existe en el asiento"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_EMPTY("RN-JOURNALENTRY-012", "error.journalEntry.empty",
            "El asiento debe tener al menos una línea"),

    ERR_JOURNALENTRY_INSUFFICIENT_LINES("RN-JOURNALENTRY-013", "error.journalEntry.insufficientLines",
            "El asiento debe tener al menos dos líneas (partida doble)"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_ALREADY_POSTED("RN-JOURNALENTRY-014", "error.journalEntry.alreadyPosted",
            "El asiento ya está contabilizado"),

    ERR_JOURNALENTRY_FUTURE_DATE("RN-JOURNALENTRY-015", "error.journalEntry.futureDate",
            "No se puede contabilizar un asiento con fecha futura"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_NOT_POSTED_REVERSAL("RN-JOURNALENTRY-016", "error.journalEntry.notPostedReversal",
            "Solo se pueden reversar asientos contabilizados"),

    ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON("RN-JOURNALENTRY-017", "error.journalEntry.reversalRequiresReason",
            "Se requiere una razón para reversar el asiento"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER("RN-JOURNALENTRY-018", "error.journalEntry.missingDocumentNumber",
            "El número de documento es obligatorio"),

    ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER("RN-JOURNALENTRY-019", "error.journalEntry.invalidDocumentNumber",
            "El número de documento debe tener al menos 1 carácter"),

    ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD("RN-JOURNALENTRY-020", "error.journalEntry.missingDescriptionField",
            "La descripción es obligatoria"),

    ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH("RN-JOURNALENTRY-021", "error.journalEntry.invalidDescriptionLength",
            "La descripción debe tener al menos 5 caracteres"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_MISSING_DATE("RN-JOURNALENTRY-022", "error.journalEntry.missingDate",
            "La fecha es obligatoria"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_MISSING_AMOUNT("RN-JOURNALENTRY-023", "error.journalEntry.missingAmount",
            "El monto es obligatorio"),



    // --- LedgerAccount ---
    ERR_ACCOUNT_INVALID_CODE_LENGTH("RN-LEDGERACCOUNT-001", "error.ledgerAccount.invalidCodeLength",
            "El código de la cuenta debe tener longitud válida (1, 2, 4, 6 u 8 dígitos)"),

    ERR_ACCOUNT_INVALID_CODE_FORMAT("RN-LEDGERACCOUNT-002", "error.ledgerAccount.invalidCodeFormat",
            "El código de la cuenta solo puede contener dígitos numéricos"),

    ERR_ACCOUNT_MISSING_NATURE("RN-LEDGERACCOUNT-003", "error.ledgerAccount.missingNature",
            "La naturaleza de la cuenta es obligatoria"),

    ERR_ACCOUNT_NOT_EDITABLE("RN-LEDGERACCOUNT-004", "error.ledgerAccount.notEditable",
            "La cuenta solo puede editarse si está activa"),

    ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON("RN-LEDGERACCOUNT-005", "error.ledgerAccount.inactivationRequiresReason",
            "La inactivación de la cuenta requiere un motivo obligatorio"),

    ERR_ACCOUNT_CANNOT_MODIFY_CODE("RN-LEDGERACCOUNT-006", "error.ledgerAccount.cannotModifyCode",
            "El código de la cuenta no puede modificarse una vez registrado"),

    ERR_ACCOUNT_REQUIRES_THIRD_PARTY("RN-LEDGERACCOUNT-007", "error.ledgerAccount.requiresThirdParty",
            "El movimiento debe cumplir requisitos de tercero si la cuenta lo requiere"),

    ERR_ACCOUNT_REQUIRES_DOCUMENT("RN-LEDGERACCOUNT-008", "error.ledgerAccount.requiresDocument",
            "El movimiento debe cumplir requisitos de documento si la cuenta lo requiere"),

    ERR_ACCOUNT_DUPLICATE_CODE("RN-LEDGERACCOUNT-009", "error.ledgerAccount.duplicateCode",
            "El código de la cuenta debe ser único por compañía"),

    ERR_ACCOUNT_ALREADY_ACTIVE("RN-LEDGERACCOUNT-010", "error.ledgerAccount.alreadyActive",
            "La cuenta ya está activa"),

    ERR_ACCOUNT_MISSING_CODE("RN-LEDGERACCOUNT-011", "error.ledgerAccount.missingCode",
            "El código de la cuenta es obligatorio"),


    //    // OpeningBalanceErrorCatalog


    ERR_OPENING_BALANCE_INVALID_AMOUNT("RN-OPENINGBALANCE-001", "error.openingBalance.invalidAmount",
            "El monto debe ser mayor a cero"),

    ERR_OPENING_BALANCE_MISSING_AMOUNT("RN-OPENINGBALANCE-002", "error.openingBalance.missingAmount",
            "El monto es obligatorio"),

    ERR_OPENING_BALANCE_MISSING_DATE("RN-OPENINGBALANCE-003", "error.openingBalance.missingDate",
            "La fecha es obligatoria"),

    ERR_OPENING_BALANCE_MISSING_ACCOUNT("RN-OPENINGBALANCE-004", "error.openingBalance.missingAccount",
            "Debe tener cuenta contable válida"),

    ERR_OPENING_BALANCE_MISSING_COMPANY("RN-OPENINGBALANCE-005", "error.openingBalance.missingCompany",
            "Debe tener compañía válida"),

    ERR_OPENING_BALANCE_IMMUTABLE("RN-OPENINGBALANCE-006", "error.openingBalance.immutable",
            "No permite edición una vez registrado (inmutable)"),

    ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY("RN-OPENINGBALANCE-007", "error.openingBalance.requiresThirdParty",
            "Si la cuenta requiere tercero, debe incluir tercero"),

    ERR_OPENING_BALANCE_DUPLICATE("RN-OPENINGBALANCE-008", "error.openingBalance.duplicate",
            "No puede registrarse saldo duplicado para misma cuenta/tercero/período"),

///  THIRDPARTIES

// ThirdPartiesErrorCatalog

ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH("RN-THIRDPARTIES-001", "error.thirdParties.invalidDocumentLength",
        "Número de documento debe tener entre 5 y 20 caracteres"),

    ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE("RN-THIRDPARTIES-002", "error.thirdParties.missingDocumentType",
            "Tipo de documento es obligatorio"),

    ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER("RN-THIRDPARTIES-003", "error.thirdParties.missingDocumentNumber",
            "Número de documento es obligatorio y único"),

    ERR_THIRD_PARTY_MISSING_TYPE("RN-THIRDPARTIES-004", "error.thirdParties.missingType",
            "Tipo de tercero es obligatorio"),

    ERR_THIRD_PARTY_NOT_EDITABLE("RN-THIRDPARTIES-005", "error.thirdParties.notEditable",
            "Solo puede editarse si está activo"),

    ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON("RN-THIRDPARTIES-006", "error.thirdParties.inactivationRequiresReason",
            "Inactivación requiere motivo obligatorio"),

    ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT("RN-THIRDPARTIES-007", "error.thirdParties.cannotModifyDocument",
            "No puede modificarse el número de documento una vez registrado"),

    ERR_THIRD_PARTY_DUPLICATE_DOCUMENT("RN-THIRDPARTIES-008", "error.thirdParties.duplicateDocument",
            "Número de documento debe ser único por compañía"),

    ERR_THIRD_PARTY_INVALID_DOCUMENT_FORMAT("RN-THIRDPARTIES-009", "error.thirdParties.invalidDocumentFormat",
            "Número de documento solo acepta caracteres alfanuméricos"),
    ERR_THIRD_PARTY_ALREADY_ACTIVE("RN-THIRDPARTIES-010", "error.thirdParties.alreadyActive",
            "El tercero ya está activo"),
    ERR_THIRD_PARTY_ALREADY_INACTIVE("RN-THIRDPARTIES-011", "error.thirdParties.alreadyInactive",
                                             "El tercero ya está inactivo"),





    // PACIENTE
    ERR_PATIENT_MISSING_REQUIRED_FIELDS(// ELIMINAR
            "RN-PATIENT-001",
            "error.patient.missingRequiredFields",
            "El paciente debe tener nombre, documento y fecha de nacimiento válida"
    ),

    ERR_PATIENT_ACTIVE_SERVICES(
            "RN-PATIENT-002",
            "error.patient.activeServices",
            "No puede desactivarse o editar si tiene citas activas o tratamientos en curso"
    ),

    ERR_PATIENT_TIME_CONFLICT(
            "RN-PATIENT-003",
            "error.patient.timeConflict",
            "El paciente no puede tener dos citas en el mismo horario"
    ),

    ERR_PATIENT_NOT_EDITABLE(// ELIMINAR
            "RN-PATIENT-004",
            "error.patient.notEditable",
            "El paciente solo puede editarse si está activo"
    ),

    ERR_PATIENT_MISSING_CONTACT(// ELIMINAR
            "RN-PATIENT-005",
            "error.patient.missingContact",
            "El paciente debe registrar al menos un medio de contacto válido"
    ),

    // bien
    ERR_PATIENT_INVALID_AGE(
            "RN-PATIENT-006",
            "error.patient.invalidAge",
            "La edad del paciente debe estar en rango válido (13-120 años)"
    ),


    ERR_PATIENT_FUTURE_BIRTHDATE(// ELIMINAR
            "RN-PATIENT-007",
            "error.patient.futureBirthdate",
            "La fecha de nacimiento del paciente no puede ser futura"
    ),

    ERR_PATIENT_MINOR_REQUIRES_GUARDIAN(
            "RN-PATIENT-008",
            "error.patient.minorRequiresGuardian",
            "Si el paciente es menor de edad, debe tener responsable legal vinculado"
    ),

    ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE(
            "RN-PATIENT-009",
            "error.patient.cannotModifyBirthdate",
            "No puede modificarse la fecha de nacimiento si el paciente tiene citas registradas"
    ),

    ERR_PATIENT_DEACTIVATION_REQUIRES_REASON(
         "RN-PATIENT-010",
         "error.patient.deactivationRequiresReason",
         "La desactivación del paciente requiere motivo obligatorio"
    ),


    ERR_PATIENT_CREATION_REQUIRES_ACTIVE_USER(// ELIMINAR
            "RN-PATIENT-011",
            "error.patient.creationRequiresActiveUser",
            "El paciente debe tener estado ACTIVO para poder ser creado"
    ),
 ERR_PATIENT_INACTIVE(// ELIMINAR
         "RN-PATIENT-012",
         "error.patient.inactive",
         "El paciente debe estar ACTIVO para realizar esta operación"
 ),
    ERR_PATIENT_NO_SHIFT_ASSIGNED( // NUEVO
            "RN-PATIENT-013",
            "error.patient.noShiftAssigned",
            "El paciente no tiene un turno asignado"
    ),

    ERR_PATIENT_SHIFT_NOT_AVAILABLE( // NUEVO
            "RN-PATIENT-014",
            "error.patient.shiftNotAvailable",
            "El turno no está disponible en el rango de fechas indicado"
    ),








// RESPONSABLE

    ERR_GUARDIAN_MISSING_PATIENT(// ELIMINAR
        "RN-GUARDIAN-001",
                "error.guardian.missingPatient",
                "No puede crearse sin vínculo legal con un paciente"
    ),

    ERR_GUARDIAN_INACTIVE(// ELIMINAR
        "RN-GUARDIAN-002",
                "error.guardian.inactive",
                "No puede autorizar tratamientos si está inactivo"
    ),

    ERR_GUARDIAN_TREATMENT_ALREADY_STARTED(
        "RN-GUARDIAN-003",
                "error.guardian.treatmentAlreadyStarted",
                "No puede revocar consentimiento si el tratamiento ya inició"
    ),

    ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE(
        "RN-GUARDIAN-004",
                "error.guardian.missingRelationshipType",
                "Debe registrar tipo de relación al crearse"
    ),

    ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS(
        "RN-GUARDIAN-005",
                "error.guardian.activeAuthorizations",
                "No puede desactivarse si tiene autorizaciones vigentes"
    ),

    ERR_GUARDIAN_NOT_EDITABLE(// ELIMINAR
        "RN-GUARDIAN-006",
                "error.guardian.notEditable",
                "Solo puede editarse si está activo"
    ),

    ERR_GUARDIAN_MISSING_CONTACT(// ELIMINAR
        "RN-GUARDIAN-007",
                "error.guardian.missingContact",
                "Debe tener al menos un medio de contacto válido"
    ),

    ERR_GUARDIAN_UNDERAGE(// ELIMINAR
        "RN-GUARDIAN-008",
                "error.guardian.underage",
                "Debe ser mayor de edad (≥ 18 años)"
    ),

    ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP(
        "RN-GUARDIAN-009",
                "error.guardian.cannotModifyRelationship",
                "No puede modificarse vínculo si ha autorizado tratamientos"
    ),
    ERR_RESPONSIBLE_INVALID_AGE(
            "RN-RESPONSIBLE-011",
            "error.responsible.invalidAge",
            "El responsable no cuenta con la edad requerida para hacerse cargo de un paciente"
    ),

    ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON(
        "RN-GUARDIAN-010",
                "error.guardian.deactivationRequiresReason",
                "La desactivación requiere motivo obligatorio"
    );







    // ⚠️ Error genérico
   // GENERIC_ERROR("ERR_GEN_001", "An unexpected error occurred.");
    private final String code;
    private final String key;
    private final String message;


    ErrorCatalog(String code, String key, String message) {
        this.code = code;
        this.key = key;
        this.message = message;

    }

    public String getKey() {return key;}

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }


}
