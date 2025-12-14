


# ERR_REPORT_CANNOT_APPROVE

- **Código:** ERR_REPORT_CANNOT_APPROVE
- **Nombre corto:** Reporte no puede aprobarse fuera de estado revisión
- **Mensaje base:** "Solo puede aprobarse si está en revisión"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta aprobar un reporte administrativo que no está en estado *UNDER_REVIEW*.  
  Protege la integridad del flujo de aprobación asegurando que solo reportes revisados puedan ser publicados, evitando saltos indebidos en el ciclo de vida.
- **Operación / Caso de uso:** `approve(approver)`
- **Regla de negocio:** RN-ADMINREPORT-004 — "Solo puede aprobarse si está en revisión"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación de flujo de estados
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Reporte con ID 654 no puede aprobarse desde estado DRAFT"`
- **Mapa a código existente:** Reemplaza `REP02` del catálogo anterior.
- **Justificación ética:** Garantiza que la aprobación sea un proceso formal y controlado, evitando que reportes sin revisión sean publicados y afecten la confiabilidad de la información gerencial.
- **Ejemplo de uso:**
  ```java
  if (report.getStatus() != ReportStatus.UNDER_REVIEW) {
      throw new InvalidReportStatusException(ErrorCatalog.ERR_REPORT_CANNOT_APPROVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `approve()` en estado DRAFT o PUBLISHED.
    - Integración: mapping HTTP → 409 Conflict.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../../decisions/accounting/ADR-Resolución%20de%20inconsistencia%20entre%20catálogo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../../arquitetura/adr/ADR-18-Simplificación%20general%20de%20jerarquía%20de%20excepciones%20en%20el%20dominio.md).

---
