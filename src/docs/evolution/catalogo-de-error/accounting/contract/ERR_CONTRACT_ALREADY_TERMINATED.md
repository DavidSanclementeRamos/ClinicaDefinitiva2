## ERR_CONTRACT_ALREADY_TERMINATED
- **Código:** ERR_CONTRACT_ALREADY_TERMINATED
- **Nombre corto:** Contrato ya terminado
- **Mensaje base:** "El contrato ya está terminado"
- **Descripción clínica:** No se pueden ejecutar operaciones sobre contratos en estado TERMINATED.
- **Operación / Caso de uso:** REACTIVAR_CONTRATO / EDITAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-012
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Estado
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Contrato 789 ya está terminado"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Evita operaciones ilegales sobre contratos concluidos.
- **Ejemplo de uso:**
  ```java
  if (status == ContractStatus.TERMINATED) throw new BusinessRuleViolationException(ERR_CONTRACT_ALREADY_TERMINATED);
  ```  
- **Pruebas mínimas requeridas:** Unitario: operación sobre contrato terminado → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---