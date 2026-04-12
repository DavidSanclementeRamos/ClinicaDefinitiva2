## ERR_COMPANY_MISSING_CONTACT

- **Código:** ERR_COMPANY_MISSING_CONTACT
- **Nombre corto:** Falta medio de contacto válido
- **Mensaje base:** "Debe registrarse al menos un medio de contacto válido (email o teléfono)"
- **Descripción clínica:**  
  Exige contar con email o teléfono para garantizar comunicación oficial y notificaciones. Sin contacto, se compromete soporte, cumplimiento y alertas regulatorias.
- **Operación / Caso de uso:** CREAR_EMPRESA (CompanyRegistered)
- **Regla de negocio:** RN-COMPANY-008 — Debe tener al menos un medio de contacto válido
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Presentación
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Empresa sin email ni teléfono al crear"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura canales de comunicación mínimos para obligaciones y soporte.
- **Ejemplo de uso:**
  ```java
  if (isBlank(company.getEmail()) && isBlank(company.getPhoneNumber())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_MISSING_CONTACT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** creación sin email y sin teléfono → excepción.
    - **Integración:** POST /companies sin contacto → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---
