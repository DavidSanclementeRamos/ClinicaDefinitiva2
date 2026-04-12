# ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE
- **Código:** ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE
- **Nombre corto:** Tipo de documento faltante
- **Mensaje base:** `"Tipo de documento es obligatorio"`
- **Descripción clínica:** No se especificó el tipo de documento, lo que impide validar y clasificar correctamente al tercero.
- **Operación / Caso de uso:** `CREAR_TERCERO`
- **Regla de negocio:** RN-THIRDPARTIES-002 — Tipo de documento obligatorio.
- **Contexto del agregado:** `USUARIO`
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 Bad Request
- **Detalle dinámico sugerido:** `"Tercero sin tipo de documento"`
- **Mapa a código existente:** Nuevo código.
- **Justificación ética:** Evita registros incompletos que dificulten la trazabilidad.
- **Ejemplo de uso:**
  ```java
  if (typeDocument == null || typeDocument.isBlank()) {
      throw new DomainException(ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (null/blank), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.


---