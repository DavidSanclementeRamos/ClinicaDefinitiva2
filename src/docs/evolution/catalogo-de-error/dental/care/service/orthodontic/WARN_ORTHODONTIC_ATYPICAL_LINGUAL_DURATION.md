## WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION

- **Código:** WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION
- **Nombre corto:** Duración mínima en brackets linguales
- **Mensaje base:** "Brackets linguales deben tener duración mínima de 18 meses"
- **Descripción clínica:**  
  Señala que los tratamientos con brackets linguales suelen requerir un periodo mínimo para lograr resultados estables; duraciones menores deben ser justificadas.
- **Operación / Caso de uso:** PLANIFICAR_TRATAMIENTO_ORTODONCIA (planOrthodonticTreatment)
- **Regla de negocio:** RN-ORTHODONTIC-006 — Advertencia de duración mínima para linguales (ver ADR-53)
- **Contexto del agregado:** TRATAMIENTO_ORTODONCIA
- **Tipo semántico:** Advertencia clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Brackets linguales con duration=12 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege la calidad del tratamiento y la expectativa del paciente al alertar sobre planes que podrían no ser adecuados para la técnica seleccionada.
- **Ejemplo de uso:**
  ```java
  if (treatment.isLingualBrackets() && durationMonths < 18) {
      log.warn(ErrorCatalog.WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** isLingualBrackets=true y duration=12 → warning.
    - **Integración:** POST /orthodontic-treatments con lingual brackets y duration corta → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Orthodontic.

---