

# ADR-XX: ERR_REPORT_MISSING_APPROVER

- **Código:** ERR_REPORT_MISSING_APPROVER
- **Nombre corto:** Aprobación requiere usuario aprobador válido
- **Mensaje base:** "La aprobación requiere usuario aprobador válido"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta aprobar un reporte administrativo sin especificar un usuario aprobador.  
  Protege la trazabilidad y la responsabilidad del proceso de publicación, asegurando que cada aprobación quede registrada con un responsable identificado.
- **Operación / Caso de uso:** `approve(approver)`
- **Regla de negocio:** RN-ADMINREPORT-011 — "Aprobación requiere usuario aprobador válido"
- **Contexto del agregado:** ADMINISTRATIVEREPORT (EDM10)
- **Tipo semántico:** Integridad / Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 (Bad Request)
- **Detalle dinámico sugerido:** `"Reporte con ID 147 no puede aprobarse sin usuario aprobador"`
- **Mapa a código existente:** Reemplaza `REP02` del catálogo anterior (cuando se usaba para aprobación inválida).
- **Justificación ética:** Garantiza que las aprobaciones sean siempre realizadas por un usuario válido, evitando publicaciones sin responsable y protegiendo la transparencia del proceso gerencial.
- **Ejemplo de uso:**
  ```java
  if (approver == null) {
      throw new InvalidAdministrativeReportException(ErrorCatalog.ERR_REPORT_MISSING_APPROVER);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: lanzar excepción al invocar `approve(null)`.
    - Integración: mapping HTTP → 400 Bad Request.
- **Changelog / versión:** 2025-12-08, Autor: David, Motivo: Refactorización catálogo de errores alineado con RN-ADMINREPORT:[ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../../../architecture/decisions/accounting/ADR-Resolución%20de%20inconsistencia%20entre%20catálogo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)  y ADR-18: [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../../architecture/adr/ADR-18-Simplificación%20general%20de%20jerarquía%20de%20excepciones%20en%20el%20dominio.md).

---
