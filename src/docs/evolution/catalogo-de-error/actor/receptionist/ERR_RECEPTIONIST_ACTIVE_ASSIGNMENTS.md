## ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS

- **Código:** ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS
- **Nombre corto:** Cambio de sede con citas activas
- **Mensaje base:** "No puede modificar sede si tiene citas asignadas en curso"
- **Descripción clínica:**  
  Impide que un recepcionista cambie de sede mientras mantiene citas activas asignadas. Protege la trazabilidad de las agendas y evita inconsistencias en la gestión clínica.
- **Operación / Caso de uso:** MODIFICAR_SEDE_RECEPCIONISTA (updateReceptionistLocation)
- **Regla de negocio:** RN-RECEPTIONIST-007 — Restricción de cambio de sede con citas activas (ver ADR-23)
- **Contexto del agregado:** RECEPCIONISTA
- **Tipo semántico:** Integridad administrativa
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Recepcionista ID 56 intentó cambiar de sede con citas activas asignadas"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que las citas se mantengan en la sede correcta y evita confusión para pacientes y odontólogos.
- **Ejemplo de uso:**
  ```java
  if (receptionist.hasActiveAssignments()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** recepcionista con citas activas → excepción.
    - **Integración:** PUT /receptionists/{id}/location con citas activas → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Receptionist.

---