## ERR_SERVICE_INACTIVE

- **Código:** ERR_SERVICE_INACTIVE
- **Nombre corto:** Servicio inactivo
- **Mensaje base:** "No se puede operar sobre un servicio inactivo"
- **Descripción:**  
  Evita operaciones (reservas, modificaciones, facturación) sobre servicios marcados como inactivos para proteger la coherencia operativa y la experiencia del paciente.
- **Operación / Caso de uso:** OPERAR_SOBRE_SERVICIO (operateOnService)
- **Regla de negocio:** RN-SERVICE-003 — Bloqueo de operaciones sobre servicios inactivos (ver ADR-110)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Integridad de estado
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "serviceId=123 status=INACTIVE operación=CREATE_APPOINTMENT"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita promesas de atención que no se podrán cumplir y protege al paciente frente a expectativas erróneas.
- **Ejemplo de uso:**
  ```java
  if (service.isInactive()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_INACTIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** service.status=INACTIVE → excepción al crear cita.
    - **Integración:** POST /services/{id}/appointments cuando service inactive → 409.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---

