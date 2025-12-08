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

    // 📒 Errores de asientos contables
    JOURNAL_ENTRY_NULL("ACC01", "error.accounting.journalEntryNull",
            "La referencia al asiento contable no puede ser nula"),

    JOURNAL_ENTRY_DUPLICATE("ACC02", "error.accounting.journalEntryDuplicate",
            "El asiento contable ya está referenciado en el reporte"),

    JOURNAL_ENTRY_NOT_FOUND("ACC03", "error.accounting.journalEntryNotFound",
            "El asiento contable no está referenciado en el reporte"),

    // 📊 Errores de indicadores
    INDICATOR_NULL("IND01", "error.report.indicatorNull",
            "El indicador no puede ser nulo"),

    INDICATOR_NOT_FOUND("IND02", "error.report.indicatorNotFound",
            "El indicador no existe en el reporte"),
    // 📂 Errores de documentos adjuntos
    ATTACHMENT_NULL("DOC01", "error.report.attachmentNull",
            "El documento no puede ser nulo"),

    ATTACHMENT_NOT_FOUND("DOC02", "error.report.attachmentNotFound",
            "El documento no existe en el reporte"),
    // 📑 Errores de estado de reporte
    REPORT_STATUS_INVALID_FOR_SUBMISSION("REP01", "error.report.invalidStatusForSubmission",
            "Solo se pueden enviar reportes en estado borrador"),
    // 📑 Errores de estado de reporte
    REPORT_STATUS_INVALID_FOR_APPROVAL("REP02", "error.report.invalidStatusForApproval",
            "Solo se pueden aprobar reportes en revisión"),
    // 📑 Errores de estado de reporte
    REPORT_STATUS_INVALID_FOR_REJECTION("REP03", "error.report.invalidStatusForRejection",
            "Solo se pueden rechazar reportes en revisión"),
    // 📑 Errores de estado de reporte (rechazo)
    REPORT_REJECTION_REASON_REQUIRED("REP04", "error.report.rejectionReasonRequired",
            "Se requiere una razón para rechazar el reporte"),
    // 📑 Errores de estado de reporte (archivo)
    REPORT_ALREADY_ARCHIVED("REP05", "error.report.alreadyArchived",
            "El reporte ya está archivado"),
    // 📑 Errores de estado de reporte (desarchivo)
    REPORT_NOT_ARCHIVED("REP06", "error.report.notArchived",
            "Solo se pueden desarchivar reportes archivados"),
    // 📑 Errores de edición de reporte
    REPORT_NOT_EDITABLE("REP07", "error.report.notEditable",
            "No se puede modificar el reporte en estado {status}"),

    // 📑 Errores de estado de reporte (modificación)
    REPORT_ARCHIVED_NOT_EDITABLE("REP08", "error.report.archivedNotEditable",
                                         "No se puede modificar un reporte archivado"),
    // 📑 Errores de completitud de reporte
    REPORT_INCOMPLETE("REP09", "error.report.incomplete",
            "El reporte debe tener al menos un asiento contable o un indicador");




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
