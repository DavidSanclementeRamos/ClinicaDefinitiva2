## ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION

- **Código:** ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION
- **Nombre corto:** Cambio de tarifa requiere justificación
- **Mensaje base:** "Cambios en tarifa requieren justificación si hay citas programadas"
- **Descripción:**  
  Obliga a registrar una justificación cuando se modifica la tarifa de un servicio que tiene citas confirmadas, para proteger a pacientes con reservas y mantener transparencia comercial.
- **Operación / Caso de uso:** CAMBIAR_TARIFA_SERVICIO (changeServiceRate)
- **Regla de negocio:** RN-SERVICE-008 — Justificación de cambios tarifarios con citas (ver ADR-114)
- **Contexto del agregado:** SERVICE / PRICE
- **Tipo semántico:** Integridad comercial
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "serviceId=444 appointments=3 rateChange=+20% justification=null"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege al paciente frente a cambios unilaterales y garantiza registro de motivos para auditoría.
- **Ejemplo de uso:**
  ```java
  if (service.hasConfirmedAppointments() && justification.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** citas confirmadas y justification="" → excepción.
    - **Integración:** PATCH /services/{id}/rate sin justification cuando hay citas → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---