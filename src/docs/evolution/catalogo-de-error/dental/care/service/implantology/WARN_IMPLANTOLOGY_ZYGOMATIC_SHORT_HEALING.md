## WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING

- **Código:** WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING
- **Nombre corto:** Cicatrización corta en zigomáticos
- **Mensaje base:** "Implantes zigomáticos requieren tiempo de cicatrización extendido (6+ meses)"
- **Descripción clínica:**  
  Señala que los implantes zigomáticos, por su complejidad anatómica y cargas biomecánicas, demandan tiempos de cicatrización más largos que los convencionales. Registrar tiempos menores a 6 meses debe activar una revisión clínica antes de avanzar.
- **Operación / Caso de uso:** REGISTRAR_IMPLANTE (registerImplant)
- **Regla de negocio:** RN-IMPLANTOLOGY-006 — Cicatrización extendida en implantes zigomáticos (ver ADR-43)
- **Contexto del agregado:** IMPLANTE
- **Tipo semántico:** Advertencia clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Implante zigomático con healingTime=4 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Promueve la seguridad del paciente evitando progresiones clínicas con tiempos insuficientes para procedimientos de alto riesgo.
- **Ejemplo de uso:**
  ```java
  if (implant.isZygomatic() && healingTime < 6) {
      log.warn(ErrorCatalog.WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** isZygomatic=true y healingTime=5 → warning.
    - **Integración:** POST /implants tipo=ZYGMATIC con healingTime=4 → 200 con warning registrado.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Implantology.

---
