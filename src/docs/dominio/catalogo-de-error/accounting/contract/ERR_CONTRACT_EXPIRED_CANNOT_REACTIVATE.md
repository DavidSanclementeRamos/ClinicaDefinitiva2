## ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE
- **Código:** ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE
- **Nombre corto:** No reactivable por vencimiento
- **Mensaje base:** "No puede reactivarse si está vencido"
- **Descripción clínica:** Un contrato suspendido pero vencido no puede reactivarse; requiere proceso formal alterno.
- **Operación / Caso de uso:** REACTIVAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-004
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Estado
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Contrato 321 vencido no reactivable"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Previene reactivaciones que rompan la legalidad de la vigencia.
- **Ejemplo de uso:**
  ```java
  if (status == SUSPENDED && isExpired()) throw new DomainAggregateException(ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: reactivar vencido → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---