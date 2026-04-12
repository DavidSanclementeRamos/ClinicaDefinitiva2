## ERR_SHIFT_LATE_MODIFICATION

- **Código:** ERR_SHIFT_LATE_MODIFICATION
- **Nombre corto:** Modificación tardía
- **Mensaje base:** "No puede modificarse si está dentro de 24h previas sin autorización"
- **Descripción clínica:**  
  Impide que se modifiquen turnos en menos de 24 horas de anticipación sin autorización especial. Protege la estabilidad de la agenda clínica.
- **Operación / Caso de uso:** MODIFICAR_TURNO (updateShift)
- **Regla de negocio:** RN-SHIFT-009 — Restricción de modificación tardía (ver ADR-24)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 403
- **Detalle dinámico sugerido:** "Turno ID 456 intentó modificarse dentro de 24h sin autorización"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza previsibilidad en la atención y reduce cancelaciones/retrabajos por cambios intempestivos.
- **Ejemplo de uso:**
  ```java
  if (shift.isWithin24h() && !hasAuthorization) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_LATE_MODIFICATION);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** modificación dentro de 24h sin autorización → excepción.
    - **Integración:** PUT /shifts/{id} → 403 si modificación tardía.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---