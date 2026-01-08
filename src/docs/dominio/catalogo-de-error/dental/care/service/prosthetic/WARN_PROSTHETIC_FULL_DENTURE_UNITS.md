## WARN_PROSTHETIC_FULL_DENTURE_UNITS

- **Código:** WARN_PROSTHETIC_FULL_DENTURE_UNITS
- **Nombre corto:** Unidades en prótesis total
- **Mensaje base:** "Prótesis total típicamente tiene 14 unidades por arcada"
- **Descripción clínica:**  
  Señala desviaciones del conteo típico de unidades en prótesis totales por arcada. Detectar menos o más unidades de lo esperado permite revisar el diseño, la indicación y la documentación clínica antes de la fabricación.
- **Operación / Caso de uso:** REGISTRAR_PRÓTESIS_TOTAL (registerFullDenture)
- **Regla de negocio:** RN-PROSTHETIC-006 — Validación de unidades para prótesis total (ver ADR-71)
- **Contexto del agregado:** PRÓTESIS_PROSTHÉTICA
- **Tipo semántico:** Advertencia de diseño clínico
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Prótesis total arcada superior con units=12"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura que el paciente reciba una prótesis con función y estética adecuadas y evita reprocesos costosos o expectativas incumplidas.
- **Ejemplo de uso:**
  ```java
  if (prosthesis.isFullDenture() && prosthesis.getUnitsPerArch() != 14) {
      log.warn(ErrorCatalog.WARN_PROSTHETIC_FULL_DENTURE_UNITS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** isFullDenture=true y unitsPerArch=12 → warning.
    - **Integración:** POST /prosthetics con tipo=FULL_DENTURE y unitsPerArch=16 → 200 con warning en logs.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Prosthetic.

---