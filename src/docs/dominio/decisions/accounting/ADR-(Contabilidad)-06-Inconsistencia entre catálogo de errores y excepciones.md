
---

# ADR-(Dominio): Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport

**Fecha:** 2025-12-08  
**Estado:** Aceptado

---

## Contexto
Durante el desarrollo del agregado **AdministrativeReport (EDM10)** se identificó una inconsistencia:
- En los **archivos de descubrimiento de reglas de negocio**, se estableció un formato estandarizado para el catálogo de errores (`ERR_REPORT_NOT_EDITABLE`, `ERR_REPORT_INCOMPLETE`, etc.).
- En el **catálogo de errores implementado en código**, se utilizó un formato diferente (`REP01`, `ACC01`, etc.), lo que generó duplicidad semántica y falta de alineación con las reglas descubiertas.
- Además, el **ADR-18** ya había simplificado la jerarquía de excepciones, eliminando las clases específicas y reemplazándolas por excepciones parametrizadas (`BusinessRuleViolationException`, `InvalidReportStatusException`, etc.).

Esto provocó que coexistieran dos convenciones distintas:
1. **Formato de errores en descubrimiento de reglas** (ERR_REPORT_XXX).
2. **Formato de errores en catálogo implementado** (REP01, ACC01, etc.).
3. **Formato de excepciones** definido en ADR-18 (simplificado y parametrizado).

---

## Decisión
- Se adopta como **formato oficial del catálogo de errores** el definido en los **archivos de descubrimiento de reglas de negocio**, dado que cada agregado ya tiene su archivo con convención predefinida y sería costoso cambiar todos.
- Se **refactoriza el catálogo de errores del agregado AdministrativeReport** para alinearlo con las reglas descubiertas (`ERR_REPORT_XXX`).
- Se **ignora el formato de excepción** de los archivos de reglas, ya que las excepciones ahora siguen lo establecido en **ADR-18** (jerarquía simplificada y parametrizada).
- Cada error del catálogo se mapea directamente a una regla descubierta (RN-ADMINREPORT-XXX).

---
## Antes (Catálogo implementado en código)

// 📒 Errores de asientos contables
JOURNAL_ENTRY_NULL("ACC01", "error.accounting.journalEntryNull",
"La referencia al asiento contable no puede ser nula"),

JOURNAL_ENTRY_DUPLICATE("ACC02", "error.accounting.journalEntryDuplicate",
"El asiento contable ya está referenciado en el reporte"),

JOURNAL_ENTRY_NOT_FOUND("ACC03", "error.accounting.journalEntryNotFound",
"El asiento contable no está referenciado en el reporte"),

// 📊 Errores de indicadores
INDICATOR_NULL("IND01", "error.report.indicatorNull",
"El indicador no puede ser nulo"),

INDICATOR_NOT_FOUND("IND02", "error.report.indicatorNotFound",
"El indicador no existe en el reporte"),

// 📂 Errores de documentos adjuntos
ATTACHMENT_NULL("DOC01", "error.report.attachmentNull",
"El documento no puede ser nulo"),

ATTACHMENT_NOT_FOUND("DOC02", "error.report.attachmentNotFound",
"El documento no existe en el reporte"),

// 📑 Errores de estado de reporte
REPORT_STATUS_INVALID_FOR_SUBMISSION("REP01", "error.report.invalidStatusForSubmission",
"Solo se pueden enviar reportes en estado borrador"),

REPORT_STATUS_INVALID_FOR_APPROVAL("REP02", "error.report.invalidStatusForApproval",
"Solo se pueden aprobar reportes en revisión"),

REPORT_STATUS_INVALID_FOR_REJECTION("REP03", "error.report.invalidStatusForRejection",
"Solo se pueden rechazar reportes en revisión"),

REPORT_REJECTION_REASON_REQUIRED("REP04", "error.report.rejectionReasonRequired",
"Se requiere una razón para rechazar el reporte"),

REPORT_ALREADY_ARCHIVED("REP05", "error.report.alreadyArchived",
"El reporte ya está archivado"),

REPORT_NOT_ARCHIVED("REP06", "error.report.notArchived",
"Solo se pueden desarchivar reportes archivados"),

REPORT_NOT_EDITABLE("REP07", "error.report.notEditable",
"No se puede modificar el reporte en estado {status}"),

REPORT_ARCHIVED_NOT_EDITABLE("REP08", "error.report.archivedNotEditable",
"No se puede modificar un reporte archivado"),

REPORT_INCOMPLETE("REP09", "error.report.incomplete",
"El reporte debe tener al menos un asiento contable o un indicador");

## Catálogo de errores refactorizado (alineado con reglas descubiertas)

```java
//  Errores de asientos contables
ERR_REPORT_DUPLICATE_JOURNAL_ENTRY("RN-ADMINREPORT-009", "error.report.duplicateJournalEntry",
        "No puede agregarse referencia duplicada a asiento contable"),

ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND("RN-ADMINREPORT-010", "error.report.journalEntryNotFound",
        "El asiento contable no está referenciado en el reporte"),


//  Errores de indicadores
ERR_REPORT_INDICATOR_NULL("RN-ADMINREPORT-XXX", "error.report.indicatorNull",
        "El indicador no puede ser nulo"),

ERR_REPORT_INDICATOR_NOT_FOUND("RN-ADMINREPORT-XXX", "error.report.indicatorNotFound",
        "El indicador no existe en el reporte"),


//  Errores de documentos adjuntos
ERR_REPORT_ATTACHMENT_NULL("RN-ADMINREPORT-XXX", "error.report.attachmentNull",
        "El documento no puede ser nulo"),

ERR_REPORT_ATTACHMENT_NOT_FOUND("RN-ADMINREPORT-XXX", "error.report.attachmentNotFound",
        "El documento no existe en el reporte"),


//  Errores de estado de reporte
ERR_REPORT_NOT_EDITABLE("RN-ADMINREPORT-001", "error.report.notEditable",
        "Solo puede editarse si está en estado DRAFT"),

ERR_REPORT_INCOMPLETE("RN-ADMINREPORT-002", "error.report.incomplete",
        "El reporte debe tener al menos un asiento contable o un indicador"),

ERR_REPORT_CANNOT_SUBMIT("RN-ADMINREPORT-003", "error.report.cannotSubmit",
        "Solo puede enviarse a revisión desde DRAFT"),

ERR_REPORT_CANNOT_APPROVE("RN-ADMINREPORT-004", "error.report.cannotApprove",
        "Solo puede aprobarse si está en revisión"),

ERR_REPORT_CANNOT_REJECT("RN-ADMINREPORT-005", "error.report.cannotReject",
        "Solo puede rechazarse si está en revisión"),

ERR_REPORT_REJECTION_REQUIRES_REASON("RN-ADMINREPORT-006", "error.report.rejectionRequiresReason",
        "Se requiere una razón para rechazar el reporte"),

ERR_REPORT_CANNOT_ARCHIVE("RN-ADMINREPORT-007", "error.report.cannotArchive",
        "Solo puede archivarse si está publicado"),

ERR_REPORT_CANNOT_UNARCHIVE("RN-ADMINREPORT-008", "error.report.cannotUnarchive",
        "Solo puede desarchivarse si está archivado"),

ERR_REPORT_MISSING_APPROVER("RN-ADMINREPORT-011", "error.report.missingApprover",
        "La aprobación requiere usuario aprobador válido");
```

---

## Consecuencias
- **Consistencia:** El catálogo de errores queda alineado con las reglas descubiertas (RN-ADMINREPORT-XXX).
- **Compatibilidad:** Las excepciones siguen el formato simplificado de ADR-18, sin romper la semántica de dominio.
- **Mantenibilidad:** Se evita tener que refactorizar todos los agregados, ya que se conserva la convención de los archivos de reglas.
- **Claridad:** Cada error está directamente trazado a una regla descubierta, facilitando auditoría y trazabilidad.

---

## Relacionados
- [AdministrativeReport (Reporte Administrativo).md](../../descubrimientos-de-reglas/accounting/AdministrativeReport%20%28Reporte%20Administrativo%29.md)
- [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../arquitetura/adr/ADR-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)

---

 Con este ADR se resuelve la inconsistencia:
- **Errores** → formato del archivo de descubrimiento de reglas.
- **Excepciones** → formato simplificado de ADR-18.

---

