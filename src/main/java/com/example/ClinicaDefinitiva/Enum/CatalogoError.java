package com.example.ClinicaDefinitiva.Enum;

public enum CatalogoError {

    METHOD_NOT_ALLOWED("ERR_HTTP_405", "Método HTTP no permitido."),
    MISSING_REQUEST_PARAM("ERR_PARAM_001", "Parámetro de solicitud faltante."),
    INVALID_JSON("ERR_JSON_001", "Cuerpo JSON mal formado."),
    INVALID_PARAMETERS("ERR_PARAM_001", "Parámetros inválidos en ruta o query."),
    TYPE_MISMATCH("ERR_TYPE_400", "Parámetro de tipo incorrecto."),
    UNSUPPORTED_MEDIA_TYPE("ERR_CONT_415", "Tipo de contenido no soportado."),
    ROUTE_NOT_FOUND("ERR_ROUTE_404", "Recurso no encontrado."),
    ACCESS_DENIED("ERR_AUTH_403", "Acceso denegado."),
    DATA_INTEGRITY_VIOLATION("ERR_DB_409", "Conflicto de integridad de datos."),
    GENERIC_ERROR("ERR_GEN_001", "Error inesperado."),
    EDAD_NO_PERMITIDA("ERR_PARAM_001", "Edad no valida para ese cargo"),

    // 🕒 Errores de Horario
    SCHEDULE_NOT_FOUND("ERR_SCH_001", "Schedule not found."),
    INVALID_SCHEDULE("ERR_SCH_002", "Invalid schedule parameters."),

    // 🔄 Errores de Turno
    SHIFT_NOT_FOUND("ERR_SHF_001", "Shift not found."),
    INVALID_SHIFT("ERR_SHF_002", "Invalid shift parameters."),

    // 👤 Errores de Usuario
    USER_NOT_FOUND("ERR_USR_001", "User not found."),
    INVALID_USER("ERR_USR_002", "Invalid user data."),

    // 🦷 Errores de Odontólogo
    DENTIST_NOT_FOUND("ERR_DEN_001", "Dentist not found."),
    INVALID_DENTIST("ERR_DEN_002", "Invalid dentist information."),

    // 🏢 Errores de Secretario
    SECRETARY_NOT_FOUND("ERR_SEC_001", "Secretary not found."),
    INVALID_SECRETARY("ERR_SEC_002", "Invalid secretary data."),

    // 🏥 Errores de Paciente
    PATIENT_NOT_FOUND("ERR_PAT_001", "Patient not found."),
    INVALID_PATIENT("ERR_PAT_002", "Invalid patient information."),

    // 👪 Errores de Responsable
    RESPONSIBLE_NOT_FOUND("ERR_RESP_001", "Responsible person not found."),
    INVALID_RESPONSIBLE("ERR_RESP_002", "Invalid responsible data."),

    ROLE_NOT_FOUND("ERR_RESP_001", "Role not found."),

    // 📞 Teléfono duplicado en persistencia
    TELEFONO_DUPLICADO("ERR_TEL_002", "Teléfono ya registrado en otra entidad"),
    DNI_DUPLICADO("ERR_DNI_002", "El Dni ya existe");

    // ⚠️ Error genérico
   // GENERIC_ERROR("ERR_GEN_001", "An unexpected error occurred.");
    private final String code;
    private final String message;

    CatalogoError (String code, String message) {
        this.code = code;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }


}
