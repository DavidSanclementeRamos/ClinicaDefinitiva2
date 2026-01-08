## ERR_SERVICE_DURATION_FACTOR_POSITIVE

- **Código:** ERR_SERVICE_DURATION_FACTOR_POSITIVE
- **Nombre corto:** Factor de duración no positivo
- **Mensaje base:** "El factor de multiplicación debe ser positivo"
- **Descripción:**  
  Valida factores aplicados a duraciones (p. ej., multiplicadores por complejidad) para evitar resultados inválidos.
- **Operación / Caso de uso:** APLICAR_FACTOR_DURACION (applyDurationFactor)
- **Regla de negocio:** RN-DURATION-008 — Factor de duración positivo (ver ADR-127)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación de cálculo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "factor recibido: 0 para serviceId=222"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita manipulaciones que produzcan duraciones nulas o negativas y comprometan la atención.
- **Ejemplo de uso:**
  ```java
  if (factor <= 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_FACTOR_POSITIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** factor=0 → excepción.
    - **Integración:** POST /services/{id}/durations/apply-factor con factor negativo → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---
