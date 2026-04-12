## ERR_SHIFT_INVALID_TIME_RANGE

- **Código:** ERR_SHIFT_INVALID_TIME_RANGE
- **Nombre corto:** Rango horario inválido
- **Mensaje base:** "La hora de inicio debe ser anterior a la hora de fin"
- **Descripción clínica:**  
  Impide registrar turnos con rangos horarios incoherentes. Protege la consistencia operativa y evita errores en la agenda clínica.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-001 — Validación de rango horario (ver ADR-24)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Turno inválido: inicio 18:00, fin 17:00"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias que afectarían la programación de citas y la confianza de pacientes.
- **Ejemplo de uso:**
  ```java
  if (start.isAfter(end)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_INVALID_TIME_RANGE);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** inicio > fin → excepción.
    - **Integración:** POST /shifts → 400 si rango inválido.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---