### ERR_SHIFT_OVERLAP_TARGET_REQUIRED

- **Código:** ERR_SHIFT_OVERLAP_TARGET_REQUIRED
- **Nombre corto:** Turno objetivo requerido para solapamiento
- **Mensaje base:** "Debe especificarse un turno válido para evaluar solapamiento"
- **Descripción clínica:**  
  Exige un turno objetivo para evaluar si hay solapamiento. Evita comparaciones nulas y asegura lógica correcta de conflictos de agenda.
- **Operación / Caso de uso:** VALIDAR_SOLAPAMIENTO_TURNO (validateShiftOverlap)
- **Regla de negocio:** RN-SHIFT-015 — Turno objetivo obligatorio para evaluación
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Sistema
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Evaluación de solapamiento sin turno objetivo"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita falsos negativos/positivos en conflictos de agenda que impactan atención a pacientes.
- **Ejemplo de uso:**
  ```java
  if (targetShift == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_OVERLAP_TARGET_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** targetShift = null → excepción.
    - **Integración:** POST /shifts/overlap-check → 400 si falta turno objetivo.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---
