### ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED

- **Código:** ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED
- **Nombre corto:** Parámetros de reprogramación requeridos
- **Mensaje base:** "Debe especificarse nueva fecha y horas de inicio y fin para reprogramar el turno"
- **Descripción clínica:**  
  Obliga a indicar parámetros completos para reprogramación del turno. Evita estados intermedios ambiguos y asegura coordinación con citas afectadas.
- **Operación / Caso de uso:** REPROGRAMAR_TURNO (rescheduleShift)
- **Regla de negocio:** RN-SHIFT-014 — Parámetros obligatorios de reprogramación
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Sistema
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Reprogramación sin nueva fecha/hora completa"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mantiene integridad del plan de atención y minimiza el impacto en pacientes por cambios incompletos.
- **Ejemplo de uso:**
  ```java
  if (newDate == null || newStart == null || newEnd == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** falta cualquier parámetro → excepción.
    - **Integración:** PUT /shifts/{id}/reschedule → 400 si parámetros incompletos.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---