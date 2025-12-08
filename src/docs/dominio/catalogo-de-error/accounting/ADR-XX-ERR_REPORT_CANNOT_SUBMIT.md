

# ADR-XX: ERR_REPORT_CANNOT_SUBMIT

- **Código:** ERR_REPORT_CANNOT_SUBMIT
- **Nombre corto:** Reporte no puede enviarse a revisión desde estado inválido
- **Mensaje base:** "Solo puede enviarse a revisión desde DRAFT"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta enviar un reporte administrativo a revisión estando en un estado distinto de borrador.  
  Protege la integridad del flujo de aprobación asegurando que solo reportes completos y en estado inicial puedan entrar en revisión.
- **Operación / Caso de uso:** `submitForReview()`
- **Regla de negocio:** RN-ADMINREPORT-003 — "Solo puede enviarse a revisión desde DRAFT"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación de flujo de estados
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Reporte con ID 321 no puede enviarse a revisión desde estado PUBLISHED"`
- **Mapa a código existente:** Reemplaza `REP01` del catálogo anterior.
- **Justificación ética:** Garantiza que los reportes no salten pasos del ciclo de vida, evitando revisiones de reportes ya publicados o archivados.
- **Ejemplo de uso:**
  ```java
  if (report.getStatus() != ReportStatus.DRAFT) {
      throw new InvalidReportStatusException(ErrorCatalog.ERR_REPORT_CANNOT_SUBMIT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `submitForReview()` en estado UNDER_REVIEW o PUBLISHED.
    - Integración: mapping HTTP → 409 Conflict.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../decisions/accounting/ADR-Resoluci%C3%B3n%20de%20inconsistencia%20entre%20cat%C3%A1logo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../arquitetura/adr/ADR-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md).

---

