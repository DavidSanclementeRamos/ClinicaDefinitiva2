## ERR_AESTHETIC_MISSING_TYPE

- **Código:** ERR_AESTHETIC_MISSING_TYPE
- **Nombre corto:** Tipo de procedimiento obligatorio
- **Mensaje base:** "El tipo de procedimiento estético es obligatorio"
- **Descripción clínica:**  
  Evita que se registren procedimientos estéticos sin especificar el tipo. Garantiza trazabilidad y seguridad clínica.
- **Operación / Caso de uso:** CREAR_PROCEDIMIENTO_ESTETICO (createAestheticProcedure)
- **Regla de negocio:** RN-AESTHETIC-001 — Validación de obligatoriedad del tipo (ver ADR-30)
- **Contexto del agregado:** PROCEDIMIENTO_ESTETICO
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Procedimiento sin tipo especificado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita registros incompletos que puedan inducir a errores clínicos o administrativos.
- **Ejemplo de uso:**
  ```java
  if (procedure.getType() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AESTHETIC_MISSING_TYPE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** procedimiento sin tipo → excepción.
    - **Integración:** POST /aesthetic-procedures sin campo type → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Aesthetic.

---