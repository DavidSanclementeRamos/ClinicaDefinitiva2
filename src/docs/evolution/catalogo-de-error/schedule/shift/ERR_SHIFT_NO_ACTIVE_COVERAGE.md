### ERR_SHIFT_NO_ACTIVE_COVERAGE

- **Código:** ERR_SHIFT_NO_ACTIVE_COVERAGE
- **Nombre corto:** Sin turno activo en horario
- **Mensaje base:** "El dentista no tiene turno activo en ese horario"
- **Descripción clínica:**  
  Impide operar (p.ej., asignar citas) en horarios sin turno activo del odontólogo. Protege la cobertura clínica y evita responsabilidades fuera de agenda.
- **Operación / Caso de uso:** VALIDAR_COBERTURA_TURNO (ensureShiftCoverage)
- **Regla de negocio:** RN-SHIFT-016 — Cobertura activa requerida
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 403
- **Detalle dinámico sugerido:** "Dentista ID 87 sin turno activo para 2025-01-10 10:00–11:00"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza atención segura y planificada, evitando prácticas fuera de cobertura aprobada.
- **Ejemplo de uso:**
  ```java
  if (!shiftCoverage.hasActiveShiftFor(dentistId, range)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_NO_ACTIVE_COVERAGE);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** rango sin cobertura → excepción.
    - **Integración:** POST /appointments → 403 si se intenta agendar sin turno activo.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.
