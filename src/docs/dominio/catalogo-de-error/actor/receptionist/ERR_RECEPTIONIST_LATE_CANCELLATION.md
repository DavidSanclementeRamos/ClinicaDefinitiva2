## ERR_RECEPTIONIST_LATE_CANCELLATION

- **Código:** ERR_RECEPTIONIST_LATE_CANCELLATION
- **Nombre corto:** Cancelación tardía de cita
- **Mensaje base:** "Solo puede cancelar citas si no están dentro de las 24h previas"
- **Descripción clínica:**  
  Impide que un recepcionista cancele citas cuando faltan menos de 24 horas para su inicio. Protege la organización de la clínica y evita que los pacientes sufran cancelaciones de último minuto.
- **Operación / Caso de uso:** CANCELAR_CITA (cancelAppointment)
- **Regla de negocio:** RN-RECEPTIONIST-003 — Restricción de cancelación tardía (ver ADR-23)
- **Contexto del agregado:** RECEPCIONISTA
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de cancelar cita ID 456 a menos de 24h de su inicio"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes tengan estabilidad en su agenda y evita cancelaciones que afecten su tratamiento.
- **Ejemplo de uso:**
  ```java
  if (appointment.isWithin24Hours()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RECEPTIONIST_LATE_CANCELLATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita dentro de 24h → excepción.
    - **Integración:** DELETE /appointments/{id} dentro de 24h → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Receptionist.

---

