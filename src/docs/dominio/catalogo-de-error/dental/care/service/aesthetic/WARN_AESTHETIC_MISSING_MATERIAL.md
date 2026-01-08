## WARN_AESTHETIC_MISSING_MATERIAL

- **Código:** WARN_AESTHETIC_MISSING_MATERIAL
- **Nombre corto:** Material no especificado
- **Mensaje base:** "Procedimientos con porcelana deberían especificar material"
- **Descripción clínica:**  
  Señala la ausencia de material especificado en procedimientos que lo requieren (p. ej., porcelana, cerámica, resina). Favorece la trazabilidad clínica y la calidad del resultado al asegurar que el material sea claro y verificable.
- **Operación / Caso de uso:** REGISTRAR_PROCEDIMIENTO_ESTETICO (registerAestheticProcedure)
- **Regla de negocio:** RN-AESTHETIC-007 — Material requerido para procedimientos con porcelana (ver ADR-32)
- **Contexto del agregado:** PROCEDIMIENTO_ESTETICO
- **Tipo semántico:** Advertencia de completitud
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Procedimiento 'Carilla de porcelana' sin material especificado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Promueve transparencia y evita riesgos de calidad o expectativas engañosas al omitir el material.

- **Ejemplo de uso:**
  ```java
  if (procedure.requiresMaterial() && procedure.getMaterial() == null) {
      log.warn(ErrorCatalog.WARN_AESTHETIC_MISSING_MATERIAL);
      // Opcional: agregar hint en respuesta para completar material
  }
  ```

- **Pruebas mínimas requeridas:**
    - **Unitario:** procedimiento que requiere material sin valor → warning.
    - **Integración:** POST /aesthetic-procedures con tipo = "porcelana" y material vacío → respuesta 200 con warning registrado.

- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Aesthetic.
