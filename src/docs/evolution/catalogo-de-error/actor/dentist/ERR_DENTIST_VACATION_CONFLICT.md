## ERR_DENTIST_VACATION_CONFLICT

- **Código:** ERR_DENTIST_VACATION_CONFLICT
- **Nombre corto:** Conflicto vacaciones con citas
- **Mensaje base:** "Hay citas agendadas que entran en conflicto con el período de vacaciones"
- **Descripción clínica:**  
  Impide registrar vacaciones en fechas donde el odontólogo ya tiene citas confirmadas. Protege la continuidad de la atención y evita cancelaciones masivas de pacientes.
- **Operación / Caso de uso:** REGISTRAR_VACACIONES (registerVacation)
- **Regla de negocio:** RN-DENTIST-013 — Validación de conflicto vacaciones-citas (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Vacaciones solicitadas 2025-12-20 a 2025-12-30 en conflicto con cita ID 456"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege a los pacientes de cancelaciones inesperadas y asegura que las vacaciones se planifiquen sin afectar citas ya confirmadas.
- **Ejemplo de uso:**
  ```java
  if (dentist.hasAppointmentsDuring(vacationPeriod)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_VACATION_CONFLICT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** vacaciones que solapan citas → excepción.
    - **Integración:** POST /dentists/{id}/vacations con citas en conflicto → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---
