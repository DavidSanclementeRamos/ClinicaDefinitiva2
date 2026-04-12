## ERR_SHIFT_ID_REQUIRED

- **Código:** ERR_SHIFT_ID_REQUIRED
- **Nombre corto:** ShiftId requerido
- **Mensaje base:** "El valor de ShiftId no puede ser nulo"
- **Descripción clínica:**  
  Impide crear o manipular turnos operativos sin un identificador válido. Protege la trazabilidad y la asignación correcta de responsabilidades.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-001 — Identificador obligatorio en Shift
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "ShiftId nulo en creación de turno"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura que cada turno sea único y rastreable, evitando pérdidas de información crítica operativa.
- **Ejemplo de uso:**
  ```java
  if (shiftId == null) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_SHIFT_ID_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** shiftId = null → excepción.
    - **Integración:** POST /shifts → 400 si falta ID.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Shift.

---
