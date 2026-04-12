## WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION

- **Código:** WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION
- **Nombre corto:** Duración atípica en alineadores
- **Mensaje base:** "Alineadores transparentes típicamente duran 12-24 meses"
- **Descripción clínica:**  
  Indica que la duración fuera del rango típico para alineadores transparentes debe revisarse por el clínico para confirmar plan y expectativas.
- **Operación / Caso de uso:** PLANIFICAR_TRATAMIENTO_ORTODONCIA (planOrthodonticTreatment)
- **Regla de negocio:** RN-ORTHODONTIC-005 — Advertencia de duración típica para alineadores (ver ADR-53)
- **Contexto del agregado:** TRATAMIENTO_ORTODONCIA
- **Tipo semántico:** Advertencia de expectativas
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Alineadores con duration=30 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Informa al equipo clínico y al paciente sobre desviaciones de la práctica habitual para asegurar revisión y consentimiento informado.
- **Ejemplo de uso:**
  ```java
  if (treatment.isAligner() && (durationMonths < 12 || durationMonths > 24)) {
      log.warn(ErrorCatalog.WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** isAligner=true y duration=30 → warning.
    - **Integración:** POST /orthodontic-treatments con alineador y duration fuera de rango → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Orthodontic.

---