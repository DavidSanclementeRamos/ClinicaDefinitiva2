

# ADR-XX: ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND

- **Código:** ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND
- **Nombre corto:** Referencia a asiento contable inexistente
- **Mensaje base:** "El asiento contable no está referenciado en el reporte"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta remover una referencia a un asiento contable que no existe dentro del reporte administrativo.  
  Protege la integridad contable evitando operaciones inválidas que podrían generar inconsistencias en la trazabilidad de los reportes financieros.
- **Operación / Caso de uso:** `removeJournalEntryReference(journalEntryId)`
- **Regla de negocio:** RN-ADMINREPORT-010 — "No puede removerse referencia inexistente"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Validación contable
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 404 (Not Found)
- **Detalle dinámico sugerido:** `"Reporte con ID 753 no contiene referencia al asiento contable 2002"`
- **Mapa a código existente:** Reemplaza `ACC03` del catálogo anterior.
- **Justificación ética:** Garantiza que las operaciones sobre referencias contables sean válidas, evitando eliminar registros inexistentes que comprometan la exactitud y confiabilidad de la información.
- **Ejemplo de uso:**
  ```java
  if (!report.getJournalEntryReferences().contains(journalEntryId)) {
      throw new InvalidAdministrativeReportException(ErrorCatalog.ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `removeJournalEntryReference()` con un ID inexistente.
    - Integración: mapping HTTP → 404 Not Found.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../../decisions/accounting/ADR-Resolución%20de%20inconsistencia%20entre%20catálogo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../../arquitetura/adr/ADR-18-Simplificación%20general%20de%20jerarquía%20de%20excepciones%20en%20el%20dominio.md).

---
