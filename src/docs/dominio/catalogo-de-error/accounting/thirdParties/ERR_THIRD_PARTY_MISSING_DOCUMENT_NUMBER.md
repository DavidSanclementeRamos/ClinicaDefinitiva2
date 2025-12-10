# ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER
- **Código:** ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER
- **Nombre corto:** Número de documento faltante
- **Mensaje base:** `"Número de documento es obligatorio y único"`
- **Descripción clínica:** El número de documento no fue proporcionado, lo que impide identificar al tercero.
- **Operación / Caso de uso:** `CREAR_TERCERO`
- **Regla de negocio:** RN-THIRDPARTIES-003 — Documento obligatorio y único.
- **Contexto del agregado:** `USUARIO`
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 Bad Request
- **Detalle dinámico sugerido:** `"Tercero sin número de documento"`
- **Mapa a código existente:** Nuevo código.
- **Justificación ética:** Garantiza identificación única y evita duplicidad.
- **Ejemplo de uso:**
  ```java
  if (docNumber == null || docNumber.isBlank()) {
      throw new DomainException(ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (null/blank), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.


---