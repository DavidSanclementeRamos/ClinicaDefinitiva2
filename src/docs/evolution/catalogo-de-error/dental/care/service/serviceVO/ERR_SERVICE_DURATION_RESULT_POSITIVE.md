## ERR_SERVICE_DURATION_RESULT_POSITIVE

- **Código:** ERR_SERVICE_DURATION_RESULT_POSITIVE
- **Nombre corto:** Resultado de duración no positivo
- **Mensaje base:** "La duración resultante debe ser positiva"
- **Descripción:**  
  Valida operaciones que combinan duraciones (suma, resta, multiplicación) y asegura que el resultado sea válido para agendamiento.
- **Operación / Caso de uso:** CALCULAR_DURACION_RESULTANTE (calculateResultingDuration)
- **Regla de negocio:** RN-DURATION-007 — Resultado de duración positivo (ver ADR-127)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación de cálculo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Resultado calculado: -30 minutos"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita programaciones inválidas y errores en la lógica de combinación de tiempos.
- **Ejemplo de uso:**
  ```java
  if (calculatedDuration <= 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_RESULT_POSITIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** operación que produce -30 → excepción.
    - **Integración:** cálculo de duración en reserva compuesta → 422 si resultado no positivo.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---