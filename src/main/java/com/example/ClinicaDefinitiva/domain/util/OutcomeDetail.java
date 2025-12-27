package com.example.ClinicaDefinitiva.domain.util;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;


public class OutcomeDetail {
    private final ErrorCatalogXD codigo;       // ej. PACIENTE_CITAS_PENDIENTES
   // private final String mensaje;      // ej. "El paciente tiene citas pendientes"
    private final Severity severidad;  // ERROR, WARNING, INFO
    private final Category categoria;  // CLINICO, ADMINISTRATIVO, TECNICO

    public OutcomeDetail(ErrorCatalogXD codigo, Severity severidad, Category categoria) {
        this.codigo = codigo;
        //this.mensaje = mensaje;
        this.severidad = severidad;
        this.categoria = categoria;
    }

    // getters
}
