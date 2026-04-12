# ERR_THIRD_PARTY_MISSING_TYPE
- **Código:** ERR_THIRD_PARTY_MISSING_TYPE
- **Nombre corto:** Tipo de tercero faltante
- **Mensaje base:** `"Tipo de tercero es obligatorio"`
- **Descripción clínica:** No se especificó el tipo de tercero, lo que impide clasificarlo correctamente en el sistema.
- **Operación / Caso de uso:** `CREAR_TERCERO`
- **Regla de negocio:** RN-THIRDPARTIES-004 — Tipo de tercero obligatorio.
- **Contexto del agregado:** `USUARIO`
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 Bad Request
- **Detalle dinámico sugerido:** `"Tercero sin tipo definido"`
- **Mapa a código existente:** Nuevo código.
- **Justificación ética:** Asegura clasificación adecuada para procesos posteriores.
- **Ejemplo de uso:**
  ```java
  if (typeThirdParties == null) {
      throw new DomainException(ERR_THIRD_PARTY_MISSING_TYPE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (null), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.


---