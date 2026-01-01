## ERR_AVAIL_ZERO_DURATION

- **Código:** ERR_AVAIL_ZERO_DURATION
- **Nombre corto:** Duración nula o negativa
- **Mensaje base:** "No puede crearse disponibilidad con duración negativa o cero"
- **Descripción clínica:**  
  Impide registrar bloques de disponibilidad sin duración real. Protege la coherencia operativa y evita registros inútiles.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-002 — Validación de duración (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Disponibilidad inválida: duración 0 minutos"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que las disponibilidades tengan sentido clínico y operativo.
- **Ejemplo de uso:**
  ```java
  if (Duration.between(start, end).isZero() || isNegative()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_ZERO_DURATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** duración = 0 → excepción.
    - **Integración:** POST /availability → 400 si duración inválida.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---