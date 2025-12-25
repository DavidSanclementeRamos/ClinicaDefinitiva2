## ERR_RECEPTIONIST_DENTIST_INACTIVE

- **Código:** ERR_RECEPTIONIST_DENTIST_INACTIVE
- **Nombre corto:** Confirmación inválida odontólogo inactivo
- **Mensaje base:** "No puede confirmar citas para odontólogos inactivos"
- **Descripción clínica:**  
  Impide que un recepcionista confirme citas asociadas a odontólogos que se encuentran desactivados o inactivos. Protege la coherencia de la agenda clínica y evita frustración en pacientes.
- **Operación / Caso de uso:** CONFIRMAR_CITA (confirmAppointment)
- **Regla de negocio:** RN-RECEPTIONIST-001 — Restricción de confirmación con odontólogo inactivo (ver ADR-23)
- **Contexto del agregado:** RECEPCIONISTA
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de confirmar cita ID 321 con odontólogo inactivo ID 45"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes solo reciban confirmaciones válidas y evita asignaciones imposibles de cumplir.
- **Ejemplo de uso:**
  ```java
  if (!dentist.isActive()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RECEPTIONIST_DENTIST_INACTIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** odontólogo inactivo → excepción.
    - **Integración:** PUT /appointments/{id}/confirm con odontólogo inactivo → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Receptionist.

---