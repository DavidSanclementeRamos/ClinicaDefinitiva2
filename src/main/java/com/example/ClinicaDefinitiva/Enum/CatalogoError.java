package com.example.ClinicaDefinitiva.Enum;

public enum CatalogoError {

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

    // ⚠️ Error genérico
    GENERIC_ERROR("ERR_GEN_001", "An unexpected error occurred.");
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
