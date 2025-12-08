


# ADR-XX: ERR_REPORT_CANNOT_REJECT

- **Código:** ERR_REPORT_CANNOT_REJECT
- **Nombre corto:** Reporte no puede rechazarse fuera de estado revisión
- **Mensaje base:** "Solo puede rechazarse si está en revisión"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta rechazar un reporte administrativo que no está en estado *UNDER_REVIEW*.  
  Protege la integridad del flujo de aprobación asegurando que solo reportes en revisión puedan ser devueltos a borrador, evitando inconsistencias en el ciclo de vida.
- **Operación / Caso de uso:** `reject(reason)`
- **Regla de negocio:** RN-ADMINREPORT-005 — "Solo puede rechazarse si está en revisión"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación de flujo de estados
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Reporte con ID 987 no puede rechazarse desde estado PUBLISHED"`
- **Mapa a código existente:** Reemplaza `REP03` del catálogo anterior.
- **Justificación ética:** Garantiza que el rechazo sea un proceso formal y controlado, evitando que reportes ya publicados o archivados sean devueltos indebidamente.
- **Ejemplo de uso:**
  ```java
  if (report.getStatus() != ReportStatus.UNDER_REVIEW) {
      throw new InvalidReportStatusException(ErrorCatalog.ERR_REPORT_CANNOT_REJECT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `reject()` en estado DRAFT o PUBLISHED.
    - Integración: mapping HTTP → 409 Conflict.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../decisions/accounting/ADR-Resoluci%C3%B3n%20de%20inconsistencia%20entre%20cat%C3%A1logo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../arquitetura/adr/ADR-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md).

---
