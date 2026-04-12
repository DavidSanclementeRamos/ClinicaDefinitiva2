

# ADR-XX: ERR_REPORT_INCOMPLETE

- **Código:** ERR_REPORT_INCOMPLETE
- **Nombre corto:** Reporte incompleto para envío a revisión
- **Mensaje base:** "El reporte debe tener al menos un asiento contable o un indicador"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta enviar un reporte administrativo a revisión sin cumplir el contenido mínimo requerido.  
  Protege la integridad gerencial al asegurar que los reportes tengan información contable o indicadores de gestión antes de entrar al flujo de aprobación.
- **Operación / Caso de uso:** `submitForReview()`
- **Regla de negocio:** RN-ADMINREPORT-002 — "Debe tener contenido mínimo para enviar a revisión"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Validación clínica / Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 (Bad Request)
- **Detalle dinámico sugerido:** `"Reporte con ID 789 enviado sin asientos contables ni indicadores"`
- **Mapa a código existente:** Reemplaza `REP09` del catálogo anterior.
- **Justificación ética:** Garantiza que los reportes no sean vacíos ni irrelevantes, evitando decisiones gerenciales basadas en información incompleta.
- **Ejemplo de uso:**
  ```java
  if (!report.isComplete()) {
      throw new InvalidAdministrativeReportException(ErrorCatalog.ERR_REPORT_INCOMPLETE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `submitForReview()` sin asientos ni indicadores.
    - Integración: mapping HTTP → 400 Bad Request.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../../../architecture/decisions/accounting/ADR-Resolución%20de%20inconsistencia%20entre%20catálogo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../../architecture/adr/ADR-18-Simplificación%20general%20de%20jerarquía%20de%20excepciones%20en%20el%20dominio.md).

---
