### ERR_SHIFT_TYPE_REQUIRED

- **Código:** ERR_SHIFT_TYPE_REQUIRED
- **Nombre corto:** Tipo de turno requerido
- **Mensaje base:** "Debe especificarse un tipo de turno válido"
- **Descripción clínica:**  
  Obliga a definir el tipo de turno (ej. consulta, procedimiento, cobertura). Evita ambigüedades que afecten recursos, equipamiento y preparación clínica.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-013 — Tipo obligatorio
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Presentación
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Turno creado sin tipo válido (valor recibido: null)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Alinea la agenda con la realidad clínica, evitando riesgos por preparación inadecuada.
- **Ejemplo de uso:**
  ```java
  if (shiftType == null || !ShiftType.isValid(shiftType)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_TYPE_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** tipo = null → excepción.
    - **Integración:** POST /shifts sin tipo → 400.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---
