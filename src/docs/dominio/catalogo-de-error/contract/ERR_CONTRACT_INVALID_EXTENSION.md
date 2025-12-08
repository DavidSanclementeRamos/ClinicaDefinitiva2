## ERR_CONTRACT_INVALID_EXTENSION
- **Código:** ERR_CONTRACT_INVALID_EXTENSION
- **Nombre corto:** Extensión inválida
- **Mensaje base:** "La extensión de vigencia solo permite fechas posteriores"
- **Descripción clínica:** La nueva fecha de fin debe ser posterior a la fecha de fin actual; de lo contrario, la extensión es inválida.
- **Operación / Caso de uso:** EXTENDER_CONTRATO
- **Regla de negocio:** RN-CONTRACT-005
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Extensión inválida: newEndDate 2025-01-01 < endDate 2025-06-01"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Asegura continuidad real de la vigencia.
- **Ejemplo de uso:**
  ```java
  if (newEndDate.isBefore(endDate)) throw new DomainAggregateException(ERR_CONTRACT_INVALID_EXTENSION);
  ```  
- **Pruebas mínimas requeridas:** Unitario: extensión con fecha anterior → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---
