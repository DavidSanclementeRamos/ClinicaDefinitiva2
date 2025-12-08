

# ADR-XX: ERR_REPORT_DUPLICATE_JOURNAL_ENTRY

- **Código:** ERR_REPORT_DUPLICATE_JOURNAL_ENTRY
- **Nombre corto:** Referencia duplicada a asiento contable
- **Mensaje base:** "No puede agregarse referencia duplicada a asiento contable"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta agregar al reporte administrativo una referencia a un asiento contable que ya está registrada.  
  Protege la integridad contable evitando duplicidades que podrían distorsionar los reportes financieros y administrativos.
- **Operación / Caso de uso:** `addJournalEntryReference(journalEntryId)`
- **Regla de negocio:** RN-ADMINREPORT-009 — "No puede agregarse referencia duplicada a asiento contable"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación contable
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Reporte con ID 258 ya contiene referencia al asiento contable 1001"`
- **Mapa a código existente:** Reemplaza `ACC02` del catálogo anterior.
- **Justificación ética:** Garantiza que cada asiento contable se registre una sola vez en el reporte, evitando duplicidades que comprometan la exactitud de la información financiera.
- **Ejemplo de uso:**
  ```java
  if (report.getJournalEntryReferences().contains(journalEntryId)) {
      throw new InvalidAdministrativeReportException(ErrorCatalog.ERR_REPORT_DUPLICATE_JOURNAL_ENTRY);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `addJournalEntryReference()` con un ID ya existente.
    - Integración: mapping HTTP → 409 Conflict.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../../decisions/accounting/ADR-Resolución%20de%20inconsistencia%20entre%20catálogo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../../arquitetura/adr/ADR-18-Simplificación%20general%20de%20jerarquía%20de%20excepciones%20en%20el%20dominio.md).

---
