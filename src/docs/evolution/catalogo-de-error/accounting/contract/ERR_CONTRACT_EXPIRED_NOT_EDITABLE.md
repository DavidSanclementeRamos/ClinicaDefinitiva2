## ERR_CONTRACT_EXPIRED_NOT_EDITABLE
- **Código:** ERR_CONTRACT_EXPIRED_NOT_EDITABLE
- **Nombre corto:** Contrato vencido no editable
- **Mensaje base:** "No se puede editar un contrato vencido"
- **Descripción clínica:** Los contratos vencidos no pueden ser modificados.
- **Operación / Caso de uso:** EDITAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-013
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Estado
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Contrato 321 vencido no editable"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Protege la integridad de contratos caducados.
- **Ejemplo de uso:**
  ```java
  if (isExpired()) throw new BusinessRuleViolationException(ERR_CONTRACT_EXPIRED_NOT_EDITABLE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: edición de contrato vencido → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---
