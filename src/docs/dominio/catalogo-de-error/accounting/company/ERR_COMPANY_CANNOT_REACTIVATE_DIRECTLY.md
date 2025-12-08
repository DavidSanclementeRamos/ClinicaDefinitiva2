## ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY

- **Código:** ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY
- **Nombre corto:** Reactivación directa prohibida
- **Mensaje base:** "Una empresa inactiva no puede reactivarse sin proceso formal"
- **Descripción clínica:**  
  Rechaza pasar de INACTIVE a ACTIVE sin ejecutar el proceso formal (auditoría, controles, validaciones). Garantiza gobernanza y cumplimiento.
- **Operación / Caso de uso:** CAMBIAR_ESTADO (updateStatus)
- **Regla de negocio:** RN-COMPANY-004 — No puede reactivarse sin proceso formal
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Empresa 123 no puede reactivarse directamente desde INACTIVE"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita elusión de controles que protegen a terceros y la legalidad operativa.
- **Ejemplo de uso:**
  ```java
  if (company.getStatus() == INACTIVE && targetStatus == ACTIVE && !company.hasFormalReactivationProcess()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** INACTIVE → ACTIVE sin proceso → excepción.
    - **Integración:** PATCH /companies/{id}/status a ACTIVE sin expediente → 409.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---