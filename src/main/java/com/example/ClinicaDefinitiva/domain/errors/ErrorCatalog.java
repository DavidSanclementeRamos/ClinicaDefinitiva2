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

   EDAD_NO_PERMITIDA("ERR_PARAM_001", "Edad no valida para ese cargo"),
    // 🕒 Errores de Disponibilidad
    SCHEDULE_NOT_FOUND("ERR_SCH_001", "Schedule not found."),
    INVALID_SCHEDULE("ERR_SCH_002", "Invalid schedule parameters."),

    // 🔄 Errores de Turno
    SHIFT_NOT_FOUND("ERR_SHF_001", "Shift not found."),
    INVALID_SHIFT("ERR_SHF_002", "Invalid shift parameters."),

    // 👤 Errores de Usuario
    USER_NOT_FOUND("ERR_USR_001", "UserIdentity not found."),
    INVALID_USER("ERR_USR_002", "Invalid user data."),
    USER_STATUS("ERR_USR_003", "Estado invalido para operation"),

    // 🦷 Errores de Odontólogo
    DENTIST_NOT_FOUND("ERR_DEN_001", "Dentist not found."),
    INVALID_DENTIST("ERR_DEN_002", "Invalid dentist information."),
    MinimumAge_Dentist("ERR_DEN_003", "Invalid Age."),

    // 🏢 Errores de Secretario
    SECRETARY_NOT_FOUND("ERR_SEC_001", "Secretary not found."),
    INVALID_SECRETARY("ERR_SEC_002", "Invalid secretary data."),

    // 🏥 Errores de Paciente
    PATIENT_NOT_FOUND("ERR_PAT_001", "Patient not found."),
    INVALID_PATIENT("ERR_PAT_002", "Invalid patient information."),
    UNASSIGNED_RESPONSIBLE_PATIENT("ERR_PAT_003", "No responsible party has been assigned"),

    // 👪 Errores de Responsable
    RESPONSIBLE_NOT_FOUND("ERR_RESP_001", "Responsible person not found."),
    INVALID_RESPONSIBLE("ERR_RESP_002", "Invalid responsible data."),

    ROLE_NOT_FOUND("ERR_RESP_001", "Role not found."),

    // 📞 Teléfono duplicado en persistencia
    TELEFONO_DUPLICADO("ERR_TEL_002", "Teléfono ya registrado en otra entidad"),
 NULL_PHONE_NUMBER("ERR_PHO_01", "Phone numbre no null"),
 BLANK_PHONE_NUMBER("ERR_PHO_03", "Phone numbre no IMPATY"),
 INVALID_PHONE_NUMBER("ERR_PHO_04", "Phone numbre INVALID"),

    DNI_DUPLICADO("ERR_DNI_002", "El Dni ya existe"),

    // semana laboral de disponibilidad
    INVALID_WEEKLY_AVAILABILITY("ERRO_WEA ", ""),


    NULL_ADDRESS("ERR_ADD_O1", "La direcion no puede ser nula"),
    BLANK_ADDRESS("ERR_ADD_O2", "La direcion no puede ser "),
    INVALID_ADDRESS("ERR_ADD_O3", "La direcion no puede ser nula"),

    NULL_AGE("ERR_AGE01","La edad no puede ser null"),
    INVALID_AGE("ERR_AGE02","La edad no puede ser invalid"),

    NULL_USER("ERR_USER01", "User no null"),
    INACTIVE_USER("ERR_USER02", "User inactivo"),
  NULL_FULL_NAME("ERR_FNM_01", "EL nombre no puede ser nulo"),
 BLANK_FULL_NAME("ERR_FNM_02", "El nombre no puede estar basio"),

    FUTURE_DATE_OF_BIRTH("ERR_FOB_01", "La fecha no puede ser futura"),
    NULL_DATE_OF_BIRTH("ERR_FOB_02", "La fecha no puede ser NULL"),
    INVALID_DATE_OF_BIRTH("ERR_FOB_03", "La fecha esta fuera del rango "),

    NULL_SECTOR("ERR_SET_01","NO NULL"),
    NOT_ALLOWED_SECTOR("ERR_SET_02","NO ALLOWED"),
    BLANK_SECTOR("ERR_SET03","NO EMTITY"),

    EMPTY_SPECIALTY_SET("ERR-SPE02", "NO NULL Y EMTITY"),
    INVALID_SPECIALTY_VALUE("ERR_01","Specialit envalid"),
    NULL_SPECIALTY("ERR_SPC_3","NO NULL"),

    START_TIME_AFTER_END_TIME_WORKING_HOURS("ERR_SPC_1","La hora de inicio debe ser anterior a la hora de fin."),
    NULL_WORKING_HOURS("ERR_SPC_1","NO NULL "),

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
