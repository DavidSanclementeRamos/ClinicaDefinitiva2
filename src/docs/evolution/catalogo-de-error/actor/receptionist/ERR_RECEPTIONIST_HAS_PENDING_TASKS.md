## ERR_RECEPTIONIST_HAS_PENDING_TASKS

- **Código:** ERR_RECEPTIONIST_HAS_PENDING_TASKS
- **Nombre corto:** Desactivación con tareas pendientes
- **Mensaje base:** "No puede desactivarse si tiene tareas pendientes"
- **Descripción clínica:**  
  Impide que un recepcionista sea desactivado mientras mantiene tareas administrativas pendientes. Protege la continuidad operativa de la clínica y asegura que no queden procesos abiertos sin responsable.
- **Operación / Caso de uso:** DESACTIVAR_RECEPCIONISTA (deactivateReceptionist)
- **Regla de negocio:** RN-RECEPTIONIST-006 — Restricción de desactivación con tareas pendientes (ver ADR-23)
- **Contexto del agregado:** RECEPCIONISTA
- **Tipo semántico:** Integridad administrativa
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Recepcionista ID 34 con tareas pendientes no puede desactivarse"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los procesos administrativos no queden incompletos y protege la atención de los pacientes.
- **Ejemplo de uso:**
  ```java
  if (receptionist.hasPendingTasks()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RECEPTIONIST_HAS_PENDING_TASKS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** recepcionista con tareas pendientes → excepción.
    - **Integración:** PUT /receptionists/{id}/deactivate con tareas pendientes → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Receptionist.

---