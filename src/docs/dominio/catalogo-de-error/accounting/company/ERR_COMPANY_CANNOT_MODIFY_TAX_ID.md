## ERR_COMPANY_CANNOT_MODIFY_TAX_ID

- **Código:** ERR_COMPANY_CANNOT_MODIFY_TAX_ID
- **Nombre corto:** NIT inmutable tras creación
- **Mensaje base:** "El NIT no puede modificarse una vez registrado"
- **Descripción clínica:**  
  Bloquea intentos de cambiar el NIT de una empresa ya creada. El NIT es el identificador fiscal y su inmutabilidad previene fraudes y pérdida de trazabilidad.
- **Operación / Caso de uso:** ACTUALIZAR_INFORMACIÓN_TRIBUTARIA (updateTaxInformation)
- **Regla de negocio:** RN-COMPANY-006 — No puede modificarse el NIT una vez registrado
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de modificar NIT de empresa 123"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mantiene continuidad fiscal y evita suplantación o reidentificación no autorizada.
- **Ejemplo de uso:**
  ```java
  if (updatePayload.containsTaxIdChange()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_CANNOT_MODIFY_TAX_ID);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** payload con cambio de NIT → excepción.
    - **Integración:** PUT /companies/{id}/tax con nuevo NIT → 409.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---