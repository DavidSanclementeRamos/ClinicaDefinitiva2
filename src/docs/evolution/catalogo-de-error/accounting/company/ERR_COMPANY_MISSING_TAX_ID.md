## ERR_COMPANY_MISSING_TAX_ID

- **Código:** ERR_COMPANY_MISSING_TAX_ID
- **Nombre corto:** Empresa sin NIT registrado
- **Mensaje base:** "La empresa debe tener NIT único y válido"
- **Descripción clínica:**  
  Ocurre al intentar crear una empresa sin NIT. El NIT es requisito legal y fiscal para identificar a la empresa ante la DIAN y otros organismos. Sin este dato, no es posible garantizar trazabilidad ni cumplimiento normativo.
- **Operación / Caso de uso:** CREAR_EMPRESA (CompanyRegistered)
- **Regla de negocio:** RN-COMPANY-001 — Debe tener NIT único y válido
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Empresa sin NIT al intentar registro"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita que entidades operen sin identificación fiscal, protegiendo la legalidad y la auditoría.
- **Ejemplo de uso:**
  ```java
  if (company.getTaxIdentificationNumber() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_MISSING_TAX_ID);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** crear empresa con NIT nulo → lanza excepción.
    - **Integración:** POST /companies sin NIT → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---
