## ERR_COMPANY_NOT_EDITABLE

- **Código:** ERR_COMPANY_NOT_EDITABLE
- **Nombre corto:** Empresa no editable en estado INACTIVE
- **Mensaje base:** "La empresa solo puede editarse si está en estado ACTIVE o SUSPENDED"
- **Descripción clínica:**  
  Previene modificaciones cuando la empresa está INACTIVE. Mantiene coherencia del ciclo de vida y evita cambios no autorizados en entidades deshabilitadas.
- **Operación / Caso de uso:** ACTUALIZAR_CONTACTO (updateContactInformation)
- **Regla de negocio:** RN-COMPANY-003 — Solo editable si está ACTIVE o SUSPENDED
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Empresa 123 no editable desde estado INACTIVE"
- **Mapa a código existente:** Sustituye InvalidCompanyStatusException en edición
- **Justificación ética:** Protege la trazabilidad y evita mutaciones en entidades bloqueadas por decisiones operativas.
- **Ejemplo de uso:**
  ```java
  if (company.getStatus() == CompanyStatus.INACTIVE) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_NOT_EDITABLE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** updateContactInformation en INACTIVE → excepción.
    - **Integración:** PUT /companies/{id}/contact en INACTIVE → 409.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---