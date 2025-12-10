# ERR_THIRD_PARTY_NOT_EDITABLE
- **Código:** ERR_THIRD_PARTY_NOT_EDITABLE
- **Nombre corto:** Tercero no editable
- **Mensaje base:** `"Solo puede editarse si está activo"`
- **Descripción clínica:** Se intentó modificar información de un tercero inactivo, lo cual está prohibido.
- **Operación / Caso de uso:** `UPDATE_CONTACT_INFORMATION`
- **Regla de negocio:** RN-THIRDPARTIES-005 — Edición solo si activo.
- **Contexto del agregado:** `USUARIO`
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 Conflict
- **Detalle dinámico sugerido:** `"Intento de edición sobre tercero inactivo ID 123"`
- **Mapa a código existente:** Reemplaza `InvalidThirdPartiesException`.
- **Justificación ética:** Evita inconsistencias en datos de terceros inactivos.
- **Ejemplo de uso:**
  ```java
  if (!this.active) {
      throw new DomainException(ERR_THIRD_PARTY_NOT_EDITABLE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (activo=false), integración (HTTP 409).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.


---