
---

## WARN_AESTHETIC_VENEER_IRREVERSIBILITY

- **Código:** WARN_AESTHETIC_VENEER_IRREVERSIBILITY
- **Nombre corto:** Irreversibilidad en carillas
- **Mensaje base:** "Carillas deben mencionar irreversibilidad en resultado esperado"
- **Descripción clínica:**  
  Advierte que los procedimientos con carillas implican cambios irreversibles en la dentadura natural del paciente. La omisión de esta información puede generar expectativas erróneas y comprometer la decisión informada del paciente.
- **Operación / Caso de uso:** REGISTRAR_RESULTADO_ESTETICO (registerAestheticResult)
- **Regla de negocio:** RN-AESTHETIC-006 — Advertencia de irreversibilidad en carillas (ver ADR-32)
- **Contexto del agregado:** PROCEDIMIENTO_ESTETICO
- **Tipo semántico:** Advertencia clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Resultado esperado sin mención de irreversibilidad en carillas"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que el paciente reciba información completa sobre la naturaleza irreversible del procedimiento, protegiendo su autonomía y evitando decisiones basadas en expectativas incompletas.

- **Ejemplo de uso:**
  ```java
  if (procedure.isVeneer() && !procedure.getExpectedResult().contains("irreversible")) {
      log.warn(ErrorCatalog.WARN_AESTHETIC_VENEER_IRREVERSIBILITY);
  }
  ```

- **Pruebas mínimas requeridas:**
    - **Unitario:** procedimiento con carillas sin mención de irreversibilidad → warning.
    - **Integración:** POST /aesthetic-procedures con resultado esperado incompleto → respuesta 200 con warning registrado.

- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Aesthetic.

---