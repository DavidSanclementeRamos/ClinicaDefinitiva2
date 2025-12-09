## ERR_CONTRACT_MISSING_END_DATE
- **Código:** ERR_CONTRACT_MISSING_END_DATE
- **Nombre corto:** Fecha de fin faltante
- **Mensaje base:** "La fecha de fin es obligatoria"
- **Descripción clínica:** Todo contrato debe tener fecha de fin definida.
- **Operación / Caso de uso:** CREAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-015
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Contrato sin fecha de fin"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Evita contratos incompletos y asegura trazabilidad.
- **Ejemplo de uso:**
  ```java
  if (endDate == null) throw new DomainAggregateException(ERR_CONTRACT_MISSING_END_DATE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: creación sin fecha de fin → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


