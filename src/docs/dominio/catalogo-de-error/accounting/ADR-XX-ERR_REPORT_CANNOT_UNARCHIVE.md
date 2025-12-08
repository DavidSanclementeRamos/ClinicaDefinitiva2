
# ADR-XX: ERR_REPORT_CANNOT_UNARCHIVE

- **Código:** ERR_REPORT_CANNOT_UNARCHIVE
- **Nombre corto:** Reporte no puede desarchivarse fuera de estado archivado
- **Mensaje base:** "Solo puede desarchivarse si está archivado"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta restaurar un reporte administrativo desde archivo sin que esté realmente en estado *ARCHIVED*.  
  Protege la coherencia del ciclo de vida asegurando que solo reportes previamente archivados puedan volver a borrador, evitando inconsistencias en la trazabilidad.
- **Operación / Caso de uso:** `unarchive()`
- **Regla de negocio:** RN-ADMINREPORT-008 — "Solo puede desarchivarse si está archivado"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación de flujo de estados
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Reporte con ID 963 no puede desarchivarse desde estado PUBLISHED"`
- **Mapa a código existente:** Reemplaza `REP06` del catálogo anterior.
- **Justificación ética:** Garantiza que los reportes archivados sean gestionados de forma controlada, evitando restauraciones indebidas que comprometan la trazabilidad y la auditoría.
- **Ejemplo de uso:**
  ```java
  if (report.getStatus() != ReportStatus.ARCHIVED) {
      throw new InvalidReportStatusException(ErrorCatalog.ERR_REPORT_CANNOT_UNARCHIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `unarchive()` en estado DRAFT o PUBLISHED.
    - Integración: mapping HTTP → 409 Conflict.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../decisions/accounting/ADR-Resoluci%C3%B3n%20de%20inconsistencia%20entre%20cat%C3%A1logo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../arquitetura/adr/ADR-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md).

---
