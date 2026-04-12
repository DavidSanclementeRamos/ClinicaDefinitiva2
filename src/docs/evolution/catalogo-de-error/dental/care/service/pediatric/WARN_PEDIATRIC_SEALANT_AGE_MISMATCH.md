## WARN_PEDIATRIC_SEALANT_AGE_MISMATCH

- **Código:** WARN_PEDIATRIC_SEALANT_AGE_MISMATCH
- **Nombre corto:** Edad atípica para sellantes
- **Mensaje base:** "Sellantes típicamente se aplican entre 6-14 años"
- **Descripción clínica:**  
  Advierte cuando se planifica o registra aplicación de sellantes fuera del rango típico, sugiriendo revisión clínica para confirmar indicación y consentimiento.
- **Operación / Caso de uso:** REGISTRAR_PROCEDIMIENTO_PEDIATRICO (registerPediatricProcedure)
- **Regla de negocio:** RN-PEDIATRIC-003 — Advertencia de edad para sellantes (ver ADR-62)
- **Contexto del agregado:** PROCEDIMIENTO_PEDIATRICO
- **Tipo semántico:** Advertencia de práctica clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Sellante programado para paciente de 5 años"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Informa sobre desviaciones de la práctica habitual para proteger al paciente y asegurar que la indicación esté justificada.
- **Ejemplo de uso:**
  ```java
  if (procedure.isSealant() && (patientAge < 6 || patientAge > 14)) {
      log.warn(ErrorCatalog.WARN_PEDIATRIC_SEALANT_AGE_MISMATCH);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** isSealant=true y age=5 → warning.
    - **Integración:** POST /pediatric/procedures con sellante y age fuera de rango → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Pediatric.

---