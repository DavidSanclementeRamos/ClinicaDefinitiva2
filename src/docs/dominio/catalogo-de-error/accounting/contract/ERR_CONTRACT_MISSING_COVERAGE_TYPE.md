## ERR_CONTRACT_MISSING_COVERAGE_TYPE
- **Código:** ERR_CONTRACT_MISSING_COVERAGE_TYPE
- **Nombre corto:** Cobertura faltante
- **Mensaje base:** "Debe tener tipo de cobertura válido"
- **Descripción clínica:** Un contrato sin tipo de cobertura definido carece de validez operacional.
- **Operación / Caso de uso:** CREAR_CONTRATO
- **Regla de negocio:** RN-CONTRACT-006
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Contrato sin coverageType"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Evita contratos ambiguos y garantiza claridad en prestaciones.
- **Ejemplo de uso:**
  ```java
  if (coverageType == null || coverageType.isBlank()) throw new DomainAggregateException(ERR_CONTRACT_MISSING_COVERAGE_TYPE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: creación sin cobertura → excepción.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---