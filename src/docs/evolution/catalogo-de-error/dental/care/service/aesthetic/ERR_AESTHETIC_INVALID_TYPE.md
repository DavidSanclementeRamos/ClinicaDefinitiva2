## ERR_AESTHETIC_INVALID_TYPE

- **Código:** ERR_AESTHETIC_INVALID_TYPE
- **Nombre corto:** Tipo de procedimiento inválido
- **Mensaje base:** "El tipo de procedimiento debe ser reconocido por el sistema"
- **Descripción clínica:**  
  Garantiza que solo se registren procedimientos estéticos validados y reconocidos.
- **Operación / Caso de uso:** CREAR_PROCEDIMIENTO_ESTETICO (createAestheticProcedure)
- **Regla de negocio:** RN-AESTHETIC-002 — Validación de catálogo de tipos (ver ADR-30)
- **Contexto del agregado:** PROCEDIMIENTO_ESTETICO
- **Tipo semántico:** Integridad de catálogo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Tipo recibido: 'XYZ' no reconocido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita confusión y asegura que los pacientes reciban procedimientos estandarizados.
- **Ejemplo de uso:**
  ```java
  if (!catalog.isValidType(procedure.getType())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AESTHETIC_INVALID_TYPE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** tipo inválido → excepción.
    - **Integración:** POST /aesthetic-procedures con tipo desconocido → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Aesthetic.

---