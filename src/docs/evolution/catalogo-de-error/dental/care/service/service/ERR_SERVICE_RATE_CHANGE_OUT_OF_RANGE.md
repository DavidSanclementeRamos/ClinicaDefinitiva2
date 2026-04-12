## ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE

- **Código:** ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE
- **Nombre corto:** Cambio de tarifa fuera de rango
- **Mensaje base:** "El cambio de tarifa debe estar dentro del rango razonable (50%-300% del valor actual)"
- **Descripción:**  
  Restringe variaciones tarifarias extremas para evitar errores de captura, prácticas comerciales abusivas o inconsistencias contables.
- **Operación / Caso de uso:** CAMBIAR_TARIFA_SERVICIO (changeServiceRate)
- **Regla de negocio:** RN-SERVICE-011 — Límite de variación tarifaria (ver ADR-116)
- **Contexto del agregado:** PRICE / SERVICE
- **Tipo semántico:** Integridad financiera
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "oldRate=100 newRate=600 (600% del valor)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege a pacientes y organización frente a cambios no justificados o errores de entrada.
- **Ejemplo de uso:**
  ```java
  double ratio = newRate / oldRate;
  if (ratio < 0.5 || ratio > 3.0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** old=100 new=600 → excepción.
    - **Integración:** PATCH /services/{id}/rate con cambio >300% → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---