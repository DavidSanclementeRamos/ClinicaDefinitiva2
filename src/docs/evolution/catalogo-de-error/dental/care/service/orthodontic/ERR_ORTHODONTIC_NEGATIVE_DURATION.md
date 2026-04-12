## ERR_ORTHODONTIC_NEGATIVE_DURATION

- **Código:** ERR_ORTHODONTIC_NEGATIVE_DURATION
- **Nombre corto:** Duración negativa
- **Mensaje base:** "La duración del tratamiento debe ser positiva"
- **Descripción clínica:**  
  Evita registros con valores negativos que comprometan la coherencia de la historia clínica y la planificación del tratamiento.
- **Operación / Caso de uso:** ACTUALIZAR_TRATAMIENTO_ORTODONCIA (updateOrthodonticTreatment)
- **Regla de negocio:** RN-ORTHODONTIC-004 — Validación de positividad de duración (ver ADR-52)
- **Contexto del agregado:** TRATAMIENTO_ORTODONCIA
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Duración recibida: -3 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mantiene integridad de datos clínicos y evita errores de captura que podrían afectar seguimiento y facturación.
- **Ejemplo de uso:**
  ```java
  if (durationMonths <= 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ORTHODONTIC_NEGATIVE_DURATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** duration=0 → excepción.
    - **Integración:** PUT /orthodontic-treatments/{id} con duration=-2 → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Orthodontic.

---