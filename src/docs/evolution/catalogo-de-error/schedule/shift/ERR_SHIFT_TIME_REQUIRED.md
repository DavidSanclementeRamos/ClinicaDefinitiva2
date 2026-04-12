### ERR_SHIFT_TIME_REQUIRED

- **Código:** ERR_SHIFT_TIME_REQUIRED
- **Nombre corto:** Horas de turno requeridas
- **Mensaje base:** "Debe especificarse hora de inicio y fin para crear un turno"
- **Descripción clínica:**  
  Obliga a registrar inicio y fin del turno. Evita bloques incompletos que comprometen disponibilidad y coordinación de citas.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-012 — Horas obligatorias
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Turno creado sin hora de inicio/fin"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura claridad operativa y justa asignación de recursos clínicos.
- **Ejemplo de uso:**
  ```java
  if (startTime == null || endTime == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_TIME_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** startTime/endTime = null → excepción.
    - **Integración:** POST /shifts sin horas → 400.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---