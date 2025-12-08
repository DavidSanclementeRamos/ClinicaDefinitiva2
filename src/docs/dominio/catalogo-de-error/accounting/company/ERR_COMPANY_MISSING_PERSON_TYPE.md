## ERR_COMPANY_MISSING_PERSON_TYPE

- **Código:** ERR_COMPANY_MISSING_PERSON_TYPE
- **Nombre corto:** Tipo de persona no especificado
- **Mensaje base:** "El tipo de persona es obligatorio"
- **Descripción clínica:**  
  La empresa debe declarar si es NATURAL o JURÍDICA. Sin esta tipificación, no se pueden aplicar reglas tributarias, responsabilidades y requisitos legales adecuados.
- **Operación / Caso de uso:** CREAR_EMPRESA (CompanyRegistered)
- **Regla de negocio:** RN-COMPANY-005 — Tipo de persona es obligatorio
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Empresa sin tipo de persona al momento de creación"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege la correcta aplicación normativa y evita clasificaciones ambiguas con impacto legal/fiscal.
- **Ejemplo de uso:**
  ```java
  if (company.getPersonType() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_MISSING_PERSON_TYPE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** crear sin tipo de persona → excepción.
    - **Integración:** POST /companies sin personType → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---