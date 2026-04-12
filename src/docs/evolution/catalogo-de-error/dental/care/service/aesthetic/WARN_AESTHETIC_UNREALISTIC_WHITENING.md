## WARN_AESTHETIC_UNREALISTIC_WHITENING

- **Código:** WARN_AESTHETIC_UNREALISTIC_WHITENING
- **Nombre corto:** Blanqueamiento irrealista
- **Mensaje base:** "Blanqueamiento no debe prometer más de 10 tonos de aclaración"
- **Descripción clínica:**  
  Advierte sobre expectativas poco realistas en tratamientos de blanqueamiento dental.
- **Operación / Caso de uso:** REGISTRAR_RESULTADO_ESTETICO (registerAestheticResult)
- **Regla de negocio:** RN-AESTHETIC-005 — Restricción de expectativas en blanqueamiento (ver ADR-32)
- **Contexto del agregado:** PROCEDIMIENTO_ESTETICO
- **Tipo semántico:** Advertencia de expectativas
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Resultado prometido: 15 tonos de aclaración"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege al paciente de promesas engañosas y asegura expectativas realistas.
- **Ejemplo de uso:**
  ```java
  if (procedure.getExpectedWhiteningTones() > 10) {
      log.warn(ErrorCatalog.WARN_AESTHETIC_UNREALISTIC_WHITENING);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** blanqueamiento con más de 10 tonos → warning.
    - **Integración:** POST /aesthetic-procedures con resultado irrealista → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Aesthetic.

---