package com.example.ClinicaDefinitiva.domain.schedule.vo;
public  enum AppointmentType {
    INITIAL_EVALUATION,     // Primera consulta o diagnóstico general
    ROUTINE_CHECKUP,        // Control periódico sin síntomas
    CLEANING,               // Profilaxis dental
    FILLING,                // Restauración por caries
    EXTRACTION,             // Extracción dental
    ROOT_CANAL,             // Tratamiento de conducto
    CROWN_PLACEMENT,        // Colocación de corona
    BRACES_ADJUSTMENT,      // Ajuste de ortodoncia
    WHITENING,              // Blanqueamiento dental
    IMPLANT_SURGERY,        // Colocación de implantes
    POST_OP_FOLLOWUP,       // Seguimiento postquirúrgico
    EMERGENCY_VISIT,        // Dolor agudo, trauma o urgencia
    CONSULTATION,           // Consulta general o segunda opinión
    ADMINISTRATIVE          // Trámite no clínico (pagos, autorizaciones)
}
