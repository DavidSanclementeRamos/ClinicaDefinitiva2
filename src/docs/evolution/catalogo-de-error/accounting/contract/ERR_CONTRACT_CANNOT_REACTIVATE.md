## ERR_CONTRACT_CANNOT_REACTIVATE
- **Código:** ERR_CONTRACT_CANNOT_REACTIVATE
- **Nombre corto:** Reactivación inválida
- **Mensaje base:** "Solo se pueden reactivar contratos suspendidos"
- **Descripción clínica:** La reactivación solo aplica a contratos en estado SUSPENDED.
- **Operación / Caso de uso:** REACTIVAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-011
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Estado
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Contrato 456 no puede reactivarse porque está en estado ACTIVE"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Protege la coherencia del ciclo de vida contractual.
- **Ejemplo de uso:**
  ```java
  if (status != ContractStatus.SUSPENDED) throw new BusinessRuleViolationException(ERR_CONTRACT_CANNOT_REACTIVATE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: reactivar contrato no suspendido → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---