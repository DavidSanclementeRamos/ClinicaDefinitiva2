# ERR_THIRD_PARTY_ALREADY_ACTIVE

- **Código:** `ERR_THIRD_PARTY_ALREADY_ACTIVE`
- **Nombre corto:** Tercero ya activo
- **Mensaje base:** `"El tercero ya está activo"`
- **Descripción clínica:**  
  Este error ocurre cuando se intenta activar un tercero que ya se encuentra en estado activo.  
  La operación es redundante y carece de sentido clínico-operacional, por lo que se rechaza para preservar la consistencia del estado del agregado.
- **Operación / Caso de uso:** `ACTIVAR_TERCERO`
- **Regla de negocio:** RN-THIRDPARTIES-010 — Activación redundante prohibida (ver ADR-18 Simplificación general de jerarquía de excepciones en el dominio).
- **Contexto del agregado:** `USUARIO` (en tu dominio, el agregado es `ThirdParties`, aquí se documenta como contexto de terceros/usuarios).
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** `ERROR`
- **HTTP sugerido:** `409 Conflict`
- **Detalle dinámico sugerido:** `"Tercero con ID 456 ya se encuentra activo"`
- **Mapa a código existente:** Reemplaza `InvalidThirdPartiesException` para este caso específico.
- **Justificación ética:** Este error protege la integridad clínica y operacional evitando duplicidad de estados y confusión en la gestión de terceros.
- **Ejemplo de uso:**
  ```java
  if (this.active) {
      throw new DomainException(ERR_THIRD_PARTY_ALREADY_ACTIVE);
  }
  this.active = true;
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: Verificar que al invocar `activate()` sobre un tercero activo se lanza la excepción esperada.
    - Integración: Validar que el mapping HTTP retorna `409 Conflict` con el mensaje internacionalizable.
- **Changelog / versión:**
    - 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.

---
