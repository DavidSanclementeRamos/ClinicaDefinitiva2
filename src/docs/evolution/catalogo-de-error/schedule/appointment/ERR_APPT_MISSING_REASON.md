## ERR_APPT_MISSING_REASON

- **Código:** ERR_APPT_MISSING_REASON
- **Nombre corto:** Motivo clínico faltante
- **Mensaje base:** "Motivo clínico es obligatorio"
- **Descripción clínica:**  
  Obliga a registrar un motivo clínico al crear o modificar una cita. Protege la trazabilidad y asegura contexto médico para justificar procedimientos y decisiones operativas.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-011 — Motivo clínico obligatorio (ver ADR-25)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Cita ID 654 creada sin motivo clínico"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Cada atención debe tener un motivo clínico claro; sin él, se compromete la transparencia y auditoría de la práctica médica.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_MISSING_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** creación de cita sin motivo → excepción.
    - **Integración:** POST /appointments → 400 cuando el motivo está vacío o ausente.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---