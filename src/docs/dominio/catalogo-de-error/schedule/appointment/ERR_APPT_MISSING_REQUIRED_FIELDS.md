## ERR_APPT_MISSING_REQUIRED_FIELDS

- **Código:** ERR_APPT_MISSING_REQUIRED_FIELDS
- **Nombre corto:** Campos obligatorios faltantes
- **Mensaje base:** "Debe tener paciente y odontólogo válidos"
- **Descripción clínica:**  
  Impide crear citas sin datos esenciales: paciente y odontólogo. Protege la integridad clínica y evita registros incompletos.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-003 — Validación de campos obligatorios (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Intento de crear cita sin paciente u odontólogo"
- **Mapa a código existente:** Nuevo código (posible separación futura en VO)
- **Justificación ética:** Evita registros clínicos inválidos que comprometan la trazabilidad de la atención.
- **Ejemplo de uso:**
  ```java
  if (patient == null || dentist == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_MISSING_REQUIRED_FIELDS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita sin paciente → excepción.
    - **Integración:** POST /appointments → 400 si faltan campos.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---
