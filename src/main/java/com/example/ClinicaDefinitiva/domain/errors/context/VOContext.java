package com.example.ClinicaDefinitiva.domain.errors.context;


/**
 * Contexto de Value Objects agrupados por módulo/dominio.
 *
 * Simplificación (ADR-50):
 * @adr ADR-50: Simplificación de VOContext y eliminación de CodeVO
 * - Un contexto por módulo, no por VO individual.
 * - El ErrorCatalog provee la especificidad del campo.
 * - Reduce fricción y evita duplicación.
 */
public enum VOContext   {

    PERSON("PERSON"),
    SCHEDULING("SCHEDULING"),
    BILLING("BILLING"),
    DENTAL_SERVICES("DENTAL_SERVICES"),
    ACCOUNTING("ACCOUNTING"),
    USER_MANAGEMENT("USER_MANAGEMENT"),
    CLINICAL_TREATMENTS("CLINICAL_TREATMENTS"),
    ACTORS("ACTORS");

    private final String code;

    VOContext(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}

