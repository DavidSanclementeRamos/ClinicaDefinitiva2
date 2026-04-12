# ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH
- **Código:** ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH
- **Nombre corto:** Documento con longitud inválida
- **Mensaje base:** `"Número de documento debe tener entre 5 y 20 caracteres"`
- **Descripción clínica:** El número de documento ingresado no cumple con la longitud mínima o máxima permitida.  
  Esto impide identificar de manera confiable al tercero y compromete la integridad de la información.
- **Operación / Caso de uso:** `CREAR_TERCERO`
- **Regla de negocio:** RN-THIRDPARTIES-001 — Validación de longitud de documento.
- **Contexto del agregado:** `USUARIO`
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 Bad Request
- **Detalle dinámico sugerido:** `"Documento con longitud 3 inválida para tercero ID 789"`
- **Mapa a código existente:** Nuevo código.
- **Justificación ética:** Garantiza que los documentos tengan formato válido para evitar registros inconsistentes.
- **Ejemplo de uso:**
  ```java
  if (docNumber.length() < 5 || docNumber.length() > 20) {
      throw new DomainException(ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (longitud <5 y >20), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.


---