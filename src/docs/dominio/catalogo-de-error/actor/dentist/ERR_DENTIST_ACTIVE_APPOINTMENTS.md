## ERR_DENTIST_ACTIVE_APPOINTMENTS

- **Código:** ERR_DENTIST_ACTIVE_APPOINTMENTS
- **Nombre corto:** Desactivación con citas activas
- **Mensaje base:** "No puede desactivarse si tiene citas activas en las próximas 24 horas"
- **Descripción clínica:**  
  Impide que un odontólogo sea desactivado si tiene citas pendientes en el corto plazo. Protege la continuidad de la atención y evita cancelaciones abruptas que afecten a pacientes.
- **Operación / Caso de uso:** DESACTIVAR_ODONTOLOGO (deactivateDentist)
- **Regla de negocio:** RN-DENTIST-003 — Restricción de desactivación con citas activas (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Odontólogo ID 87 con citas activas hasta mañana"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes no pierdan atención médica por desactivaciones intempestivas.
- **Ejemplo de uso:**
  ```java
  if (dentist.hasActiveAppointmentsWithin(24)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_ACTIVE_APPOINTMENTS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** odontólogo con citas en 24h → excepción.
    - **Integración:** PUT /dentists/{id}/deactivate con citas activas → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---

