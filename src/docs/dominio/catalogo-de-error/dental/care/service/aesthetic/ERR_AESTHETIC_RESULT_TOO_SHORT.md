## ERR_AESTHETIC_RESULT_TOO_SHORT

- **Código:** ERR_AESTHETIC_RESULT_TOO_SHORT
- **Nombre corto:** Resultado esperado demasiado corto
- **Mensaje base:** "El resultado esperado debe tener al menos 10 caracteres si se especifica"
- **Descripción clínica:**  
  Evita resultados ambiguos o poco claros en procedimientos estéticos.
- **Operación / Caso de uso:** CREAR_PROCEDIMIENTO_ESTETICO (createAestheticProcedure)
- **Regla de negocio:** RN-AESTHETIC-004 — Restricción de longitud mínima en resultado (ver ADR-31)
- **Contexto del agregado:** PROCEDIMIENTO_ESTETICO
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Resultado esperado: 'Mejorar' con longitud 8"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita expectativas poco claras que puedan inducir a error o insatisfacción del paciente.
- **Ejemplo de uso:**
  ```java
  if (procedure.getExpectedResult() != null && procedure.getExpectedResult().length() < 10) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AESTHETIC_RESULT_TOO_SHORT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** resultado con menos de 10 caracteres → excepción.
    - **Integración:** POST /aesthetic-procedures con resultado corto → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Aesthetic.

---