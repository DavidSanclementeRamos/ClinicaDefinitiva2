### ERR_SHIFT_DATE_REQUIRED

- **Código:** ERR_SHIFT_DATE_REQUIRED
- **Nombre corto:** Fecha de turno requerida
- **Mensaje base:** "Debe especificarse una fecha válida para crear un turno"
- **Descripción clínica:**  
  Obliga a indicar la fecha del turno para su programación. Evita registros ambiguos y asegura coherencia con la agenda diaria de la clínica.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-011 — Fecha obligatoria
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Turno creado sin fecha (valor recibido: null)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita confusión en programación y reduce riesgo de errores operativos que afectan a pacientes.
- **Ejemplo de uso:**
  ```java
  if (shiftDate == null || !shiftDate.isValid()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_DATE_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** fecha = null → excepción.
    - **Integración:** POST /shifts sin fecha → 400.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---