## ERR_SHIFT_ZERO_DURATION

- **Código:** ERR_SHIFT_ZERO_DURATION
- **Nombre corto:** Duración nula o negativa
- **Mensaje base:** "No puede tener duración negativa o cero"
- **Descripción clínica:**  
  Impide registrar turnos sin duración real. Protege la coherencia operativa y evita registros inútiles.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-008 — Validación de duración (ver ADR-24)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Turno inválido: duración 0 minutos"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los turnos tengan sentido clínico y operativo.
- **Ejemplo de uso:**
  ```java
  if (Duration.between(start, end).isZero() || isNegative()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_ZERO_DURATION);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** duración = 0 → excepción.
    - **Integración:** POST /shifts → 400 si duración inválida.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---