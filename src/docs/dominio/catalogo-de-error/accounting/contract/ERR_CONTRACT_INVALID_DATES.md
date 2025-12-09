## ERR_CONTRACT_INVALID_DATES
- **Código:** ERR_CONTRACT_INVALID_DATES
- **Nombre corto:** Fechas inválidas
- **Mensaje base:** "La fecha de fin debe ser posterior a la fecha de inicio"
- **Descripción clínica:** Un contrato no puede registrarse si su fecha de fin es anterior a la fecha de inicio. Garantiza coherencia temporal y validez jurídica.
- **Operación / Caso de uso:** CREAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-001
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Contrato 123: endDate 2025-01-01 < startDate 2025-02-01"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Evita contratos inválidos y protege la trazabilidad temporal.
- **Ejemplo de uso:**
  ```java
  if (endDate.isBefore(startDate)) throw new DomainAggregateException(ERR_CONTRACT_INVALID_DATES);
  ```  
- **Pruebas mínimas requeridas:** Unitario: creación con fechas invertidas → excepción. Integración: POST /contracts → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.

---