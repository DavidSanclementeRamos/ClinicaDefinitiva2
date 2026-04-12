## ERR_SHIFT_OVERLAP_CONFLICT

- **Código:** ERR_SHIFT_OVERLAP_CONFLICT
- **Nombre corto:** Conflicto de solapamiento
- **Mensaje base:** "No puede solaparse con otro turno del mismo profesional"
- **Descripción clínica:**  
  Impide que un odontólogo registre dos turnos que se superpongan. Protege la coherencia de la agenda y evita conflictos operativos.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-003 — Restricción de solapamiento (ver ADR-24)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Odontólogo ID 87 ya tiene turno en el rango solicitado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita errores administrativos y garantiza que cada turno sea único y válido.
- **Ejemplo de uso:**
  ```java
  if (dentist.hasShiftOverlap(newShift)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_OVERLAP_CONFLICT);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** turno solapado → excepción.
    - **Integración:** POST /shifts → 409 si conflicto.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---