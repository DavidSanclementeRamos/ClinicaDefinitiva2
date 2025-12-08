## ERR_CONTRACT_NOT_EDITABLE
- **Código:** ERR_CONTRACT_NOT_EDITABLE
- **Nombre corto:** No editable
- **Mensaje base:** "Solo puede editarse si está en estado ACTIVE y no vencido"
- **Descripción clínica:** Contratos en estado distinto de ACTIVE o vencidos no admiten edición, asegurando coherencia del ciclo de vida.
- **Operación / Caso de uso:** EDITAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-002
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Autorización / Estado
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Contrato 456 no editable: status=SUSPENDED, expired=false"
- **Mapa a código existente:** Sustituye validaciones ad-hoc
- **Justificación ética:** Impide modificaciones ilegales sobre estados no permitidos.
- **Ejemplo de uso:**
  ```java
  if (status != ACTIVE || isExpired()) throw new BusinessRuleViolationException(ERR_CONTRACT_NOT_EDITABLE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: edición con estado inválido → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.

---