## WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS

- **Código:** WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS
- **Nombre corto:** Puente con unidades insuficientes
- **Mensaje base:** "Puente fijo típicamente tiene al menos 3 unidades"
- **Descripción clínica:**  
  Advierte cuando se intenta registrar un puente fijo con menos de tres unidades, lo que sugiere un diseño inadecuado o un error de captura. Un puente con menos de tres unidades puede no cumplir criterios biomecánicos mínimos para soporte y estabilidad.
- **Operación / Caso de uso:** REGISTRAR_PRÓTESIS_PUENTE (registerBridgeProsthesis)
- **Regla de negocio:** RN-PROSTHETIC-007 — Validación de unidades mínimas para puente (ver ADR-72)
- **Contexto del agregado:** PRÓTESIS_PROSTHÉTICA
- **Tipo semántico:** Advertencia de seguridad estructural
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Puente con units=2 para sector posterior"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege la integridad funcional del tratamiento y evita procedimientos que puedan fallar prematuramente, afectando la salud y recursos del paciente.
- **Ejemplo de uso:**
  ```java
  if (prosthesis.isBridge() && prosthesis.getUnits() < 3) {
      log.warn(ErrorCatalog.WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** isBridge=true y units=2 → warning.
    - **Integración:** POST /prosthetics con tipo=BRIDGE y units=1 → 200 con warning y revisión requerida.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Prosthetic.

---