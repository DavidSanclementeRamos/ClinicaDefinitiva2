## ERR_CONTRACT_MISSING_START_DATE
- **Código:** ERR_CONTRACT_MISSING_START_DATE
- **Nombre corto:** Fecha de inicio faltante
- **Mensaje base:** "La fecha de inicio es obligatoria"
- **Descripción clínica:** Todo contrato debe tener fecha de inicio definida.
- **Operación / Caso de uso:** CREAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-014
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Contrato sin fecha de inicio"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Evita contratos incompletos y asegura trazabilidad.
- **Ejemplo de uso:**
  ```java
  if (startDate == null) throw new DomainAggregateException(ERR_CONTRACT_MISSING_START_DATE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: creación sin fecha de inicio → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---