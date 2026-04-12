## ERR_CONTRACT_CANNOT_SUSPEND
- **Código:** ERR_CONTRACT_CANNOT_SUSPEND
- **Nombre corto:** Suspensión inválida
- **Mensaje base:** "Solo puede suspenderse si está en estado ACTIVE"
- **Descripción clínica:** La suspensión solo aplica a contratos activos; otros estados violan el flujo operativo.
- **Operación / Caso de uso:** SUSPENDER_CONTRATO
- **Regla de negocio:** RN-CONTRACT-003
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Estado
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Contrato 789: intento de suspensión con estado TERMINATED"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Mantiene la integridad del ciclo de vida contractual.
- **Ejemplo de uso:**
  ```java
  if (status != ACTIVE) throw new BusinessRuleViolationException(ERR_CONTRACT_CANNOT_SUSPEND);
  ```  
- **Pruebas mínimas requeridas:** Unitario: suspensión en estado no ACTIVE → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---