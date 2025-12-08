## ERR_CONTRACT_MISSING_NEW_END_DATE
- **Código:** ERR_CONTRACT_MISSING_NEW_END_DATE
- **Nombre corto:** Nueva fecha faltante
- **Mensaje base:** "La nueva fecha de fin es obligatoria"
- **Descripción clínica:** No se puede extender un contrato sin especificar la nueva fecha de fin. Esto asegura continuidad temporal y evita inconsistencias operativas.
- **Operación / Caso de uso:** EXTENDER_CONTRATO
- **Regla de negocio:** RN-CONTRACT-009
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Extensión de contrato 123 sin nueva fecha de fin"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Garantiza que las extensiones sean claras y verificables.
- **Ejemplo de uso:**
  ```java
  if (newEndDate == null) throw new DomainAggregateException(ERR_CONTRACT_MISSING_NEW_END_DATE);
  ```  
- **Pruebas mínimas requeridas:** Unitario: extensión sin fecha → excepción. Integración: PATCH /contracts/{id}/extend sin newEndDate → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Contract.


---
