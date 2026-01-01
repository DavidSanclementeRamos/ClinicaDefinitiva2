## ERR_SHIFT_ID_BLANK

- **Código:** ERR_SHIFT_ID_BLANK
- **Nombre corto:** ShiftId vacío
- **Mensaje base:** "El valor de ShiftId no puede estar vacío"
- **Descripción clínica:**  
  Impide que el identificador de turno sea una cadena vacía o con solo espacios. Protege la integridad de datos y evita registros inválidos.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-002 — Identificador no vacío en Shift
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "ShiftId vacío en creación de turno (valor recibido: '')"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias en la trazabilidad operativa y confusiones en auditorías.
- **Ejemplo de uso:**
  ```java
  if (shiftId == null || shiftId.trim().isEmpty()) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_SHIFT_ID_BLANK);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** shiftId = "" → excepción.
    - **Integración:** POST /shifts → 400 si ID vacío.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Shift.

