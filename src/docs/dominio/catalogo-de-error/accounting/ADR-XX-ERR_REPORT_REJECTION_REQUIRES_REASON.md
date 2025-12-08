

# ADR-XX: ERR_REPORT_REJECTION_REQUIRES_REASON

- **Código:** ERR_REPORT_REJECTION_REQUIRES_REASON
- **Nombre corto:** Rechazo de reporte requiere motivo obligatorio
- **Mensaje base:** "Se requiere una razón para rechazar el reporte"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta rechazar un reporte administrativo sin proporcionar un motivo válido.  
  Protege la trazabilidad y la transparencia del proceso de revisión, asegurando que cada rechazo quede documentado con una justificación clara.
- **Operación / Caso de uso:** `reject(reason)`
- **Regla de negocio:** RN-ADMINREPORT-006 — "El rechazo requiere motivo obligatorio"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 (Bad Request)
- **Detalle dinámico sugerido:** `"Reporte con ID 852 rechazado sin motivo"`
- **Mapa a código existente:** Reemplaza `REP04` del catálogo anterior.
- **Justificación ética:** Garantiza que los rechazos no sean arbitrarios y que exista trazabilidad para auditoría y control gerencial.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new InvalidAdministrativeReportException(ErrorCatalog.ERR_REPORT_REJECTION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `reject(null)` o `reject("")`.
    - Integración: mapping HTTP → 400 Bad Request.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../decisions/accounting/ADR-Resoluci%C3%B3n%20de%20inconsistencia%20entre%20cat%C3%A1logo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../arquitetura/adr/ADR-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md).

---
