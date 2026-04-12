## ERR_COMPANY_MISSING_INCORPORATION_DATE

- **Código:** ERR_COMPANY_MISSING_INCORPORATION_DATE
- **Nombre corto:** Falta fecha de constitución
- **Mensaje base:** "La fecha de constitución es obligatoria"
- **Descripción clínica:**  
  La empresa debe registrar su fecha de constitución para validar existencia legal y temporalidad del registro. Sin ella, no se puede auditar ni cumplir requisitos regulatorios.
- **Operación / Caso de uso:** CREAR_EMPRESA (CompanyRegistered)
- **Regla de negocio:** RN-COMPANY-007 — Fecha de constitución es obligatoria
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Empresa sin fecha de constitución en registro"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura la veracidad documental y habilita auditorías temporales.
- **Ejemplo de uso:**
  ```java
  if (company.getIncorporationDate() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_MISSING_INCORPORATION_DATE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** creación con fecha nula → excepción.
    - **Integración:** POST /companies sin incorporationDate → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---
[Índice de ADRs — Catálogo de Errores Company (Empresa).md](%C3%8Dndice%20de%20ADRs%20%E2%80%94%20Cat%C3%A1logo%20de%20Errores%20Company%20%28Empresa%29.md)
