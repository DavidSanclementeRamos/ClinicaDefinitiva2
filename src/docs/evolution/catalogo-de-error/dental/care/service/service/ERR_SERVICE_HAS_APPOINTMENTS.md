## ERR_SERVICE_HAS_APPOINTMENTS

- **Código:** ERR_SERVICE_HAS_APPOINTMENTS
- **Nombre corto:** Tiene citas próximas
- **Mensaje base:** "No puede desactivarse porque tiene citas programadas en las próximas 48 horas"
- **Descripción:**  
  Impide desactivar un servicio cuando existen citas confirmadas en ventana crítica (48 horas) para proteger la continuidad asistencial y evitar cancelaciones de último minuto.
- **Operación / Caso de uso:** DESACTIVAR_SERVICIO (deactivateService)
- **Regla de negocio:** RN-SERVICE-005 — Bloqueo de desactivación con citas próximas (ver ADR-112)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Integridad temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "serviceId=222 nextAppointment=2026-01-09T10:00"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege al paciente y al equipo evitando cancelaciones que afecten acceso a la atención y obligaciones contractuales.
- **Ejemplo de uso:**
  ```java
  if (service.hasAppointmentsWithin(Duration.ofHours(48))) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_HAS_APPOINTMENTS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita a 24h → excepción al desactivar.
    - **Integración:** DELETE /services/{id} cuando existen citas en 48h → 409.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

--