

---

# ADR-XX: ERR_REPORT_NOT_EDITABLE

- **Código:** ERR_REPORT_NOT_EDITABLE
- **Nombre corto:** Reporte no editable fuera de estado DRAFT
- **Mensaje base:** "Solo puede editarse si está en estado DRAFT"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta modificar un reporte administrativo que ya no está en estado borrador.  
  Protege la integridad de la información evitando cambios en reportes en revisión, publicados o archivados.
- **Operación / Caso de uso:** `updateInformation`, `addJournalEntryReference`, `removeJournalEntryReference`, `addIndicator`, `removeIndicator`, `addAttachment`, `removeAttachment`
- **Regla de negocio:** RN-ADMINREPORT-001 — "Solo puede editarse si está en estado DRAFT"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Reporte con ID 456 no editable en estado UNDER_REVIEW"`
- **Mapa a código existente:** Reemplaza `REP07` del catálogo anterior.
- **Justificación ética:** Garantiza que la información gerencial no sea alterada una vez que entra en revisión o publicación, protegiendo la trazabilidad y la confianza en los reportes.
- **Ejemplo de uso:**
  ```java
  if (!report.isEditable()) {
      throw new InvalidReportStatusException(ErrorCatalog.ERR_REPORT_NOT_EDITABLE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al intentar editar en estado UNDER_REVIEW.
    - Integración: mapping HTTP → 409 Conflict.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../../decisions/accounting/ADR-Resolución%20de%20inconsistencia%20entre%20catálogo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../../arquitetura/adr/ADR-18-Simplificación%20general%20de%20jerarquía%20de%20excepciones%20en%20el%20dominio.md).

---

