# ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON
- **Código:** ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON
- **Nombre corto:** Inactivación sin motivo
- **Mensaje base:** `"Inactivación requiere motivo obligatorio"`
- **Descripción clínica:** Se intentó inactivar un tercero sin proporcionar motivo, lo que impide trazabilidad.
- **Operación / Caso de uso:** `INACTIVATE_TERCERO`
- **Regla de negocio:** RN-THIRDPARTIES-006 — Motivo obligatorio para inactivación.
- **Contexto del agregado:** `USUARIO`
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 Bad Request
- **Detalle dinámico sugerido:** `"Inactivación sin motivo para tercero ID 321"`
- **Mapa a código existente:** Nuevo código.
- **Justificación ética:** Garantiza trazabilidad y responsabilidad en decisiones de inactivación.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new DomainException(ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (null/blank), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.


---