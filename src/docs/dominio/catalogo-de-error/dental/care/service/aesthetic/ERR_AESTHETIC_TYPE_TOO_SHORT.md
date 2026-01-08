## ERR_AESTHETIC_TYPE_TOO_SHORT

- **Código:** ERR_AESTHETIC_TYPE_TOO_SHORT
- **Nombre corto:** Tipo demasiado corto
- **Mensaje base:** "El tipo de procedimiento debe tener al menos 3 caracteres"
- **Descripción clínica:**  
  Evita registros con descripciones insuficientes que dificulten la comprensión clínica.
- **Operación / Caso de uso:** CREAR_PROCEDIMIENTO_ESTETICO (createAestheticProcedure)
- **Regla de negocio:** RN-AESTHETIC-003 — Restricción de longitud mínima (ver ADR-31)
- **Contexto del agregado:** PROCEDIMIENTO_ESTETICO
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Tipo recibido: 'Pe' con longitud 2"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza claridad y evita ambigüedad en registros clínicos.
- **Ejemplo de uso:**
  ```java
  if (procedure.getType().length() < 3) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AESTHETIC_TYPE_TOO_SHORT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** tipo con 2 caracteres → excepción.
    - **Integración:** POST /aesthetic-procedures con tipo corto → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Aesthetic.

---
