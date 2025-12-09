## ERR_CONTRACT_TERMINATION_REQUIRES_REASON
- **Código:** ERR_CONTRACT_TERMINATION_REQUIRES_REASON
- **Nombre corto:** Terminación sin motivo
- **Mensaje base:** "La terminación requiere motivo obligatorio"
- **Descripción clínica:** No se permite terminar un contrato sin motivo; es imprescindible para trazabilidad y auditoría.
- **Operación / Caso de uso:** TERMINAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-008
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Terminate contrato 654 sin reason"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Garantiza transparencia y responsabilidad en la terminación.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) throw new DomainAggregateException(ERR_CONTRACT_TERMINATION_REQUIRES_REASON);
  ```  
- **Pruebas mínimas requeridas:** Unitario: terminación sin motivo → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---