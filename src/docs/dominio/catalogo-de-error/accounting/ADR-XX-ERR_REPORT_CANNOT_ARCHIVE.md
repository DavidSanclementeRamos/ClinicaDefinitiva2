

# ADR-XX: ERR_REPORT_CANNOT_ARCHIVE

- **Código:** ERR_REPORT_CANNOT_ARCHIVE
- **Nombre corto:** Reporte no puede archivarse fuera de estado publicado
- **Mensaje base:** "Solo puede archivarse si está publicado"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta archivar un reporte administrativo que no está en estado *PUBLISHED*.  
  Protege la integridad del ciclo de vida asegurando que solo reportes aprobados y publicados puedan ser enviados a archivo, evitando pérdida de trazabilidad o cierre prematuro.
- **Operación / Caso de uso:** `archive()`
- **Regla de negocio:** RN-ADMINREPORT-007 — "Solo puede archivarse si está publicado"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación de flujo de estados
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Reporte con ID 741 no puede archivarse desde estado UNDER_REVIEW"`
- **Mapa a código existente:** Reemplaza `REP05` del catálogo anterior.
- **Justificación ética:** Garantiza que los reportes archivados sean únicamente aquellos que ya fueron aprobados y publicados, protegiendo la trazabilidad y evitando ocultar información sin validación previa.
- **Ejemplo de uso:**
  ```java
  if (report.getStatus() != ReportStatus.PUBLISHED) {
      throw new InvalidReportStatusException(ErrorCatalog.ERR_REPORT_CANNOT_ARCHIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `archive()` en estado DRAFT o UNDER_REVIEW.
    - Integración: mapping HTTP → 409 Conflict.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../decisions/accounting/ADR-Resoluci%C3%B3n%20de%20inconsistencia%20entre%20cat%C3%A1logo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../arquitetura/adr/ADR-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md).

---
