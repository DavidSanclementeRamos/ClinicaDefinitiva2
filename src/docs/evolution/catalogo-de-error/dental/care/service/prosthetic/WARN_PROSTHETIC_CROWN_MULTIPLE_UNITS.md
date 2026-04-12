## WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS

- **Código:** WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS
- **Nombre corto:** Corona con múltiples unidades
- **Mensaje base:** "Corona individual típicamente tiene 1 unidad"
- **Descripción clínica:**  
  Advierte cuando se registra una corona como múltiple unidades; las coronas individuales suelen corresponder a una sola unidad protésica. Detectar múltiples unidades en este contexto ayuda a revisar la indicación y el plan protésico para evitar errores de facturación o planificación.
- **Operación / Caso de uso:** REGISTRAR_PRÓTESIS_CORONA (registerCrownProsthesis)
- **Regla de negocio:** RN-PROSTHETIC-005 — Validación de unidades para corona (ver ADR-70)
- **Contexto del agregado:** PRÓTESIS_PROSTHÉTICA
- **Tipo semántico:** Advertencia de consistencia
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Corona registrada con unidades=3 para pieza 1.1"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita sobretratamientos, errores administrativos y garantiza transparencia en la comunicación con el paciente sobre el alcance real del procedimiento.
- **Ejemplo de uso:**
  ```java
  if (prosthesis.isCrown() && prosthesis.getUnits() > 1) {
      log.warn(ErrorCatalog.WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** isCrown=true y units=2 → warning.
    - **Integración:** POST /prosthetics con tipo=CROWN y units=3 → 200 con warning en logs.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Prosthetic.

---