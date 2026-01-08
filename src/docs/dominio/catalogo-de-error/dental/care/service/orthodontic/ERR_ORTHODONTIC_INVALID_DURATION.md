## ERR_ORTHODONTIC_INVALID_DURATION

- **Código:** ERR_ORTHODONTIC_INVALID_DURATION
- **Nombre corto:** Duración fuera de rango
- **Mensaje base:** "La duración del tratamiento debe estar entre 6 y 48 meses"
- **Descripción clínica:**  
  Asegura que la duración planificada del tratamiento ortodóntico se mantenga dentro de límites clínicamente razonables para eficacia y seguimiento.
- **Operación / Caso de uso:** PLANIFICAR_TRATAMIENTO_ORTODONCIA (planOrthodonticTreatment)
- **Regla de negocio:** RN-ORTHODONTIC-002 — Rango válido de duración (ver ADR-51)
- **Contexto del agregado:** TRATAMIENTO_ORTODONCIA
- **Tipo semántico:** Validación de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Duración recibida: 60 meses fuera de rango"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita promesas de tratamiento poco realistas o planes que puedan exponer al paciente a riesgos por tratamientos excesivamente largos o cortos.
- **Ejemplo de uso:**
  ```java
  if (durationMonths < 6 || durationMonths > 48) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ORTHODONTIC_INVALID_DURATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** duration=5 → excepción.
    - **Integración:** POST /orthodontic-treatments con duration=60 → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Orthodontic.

---