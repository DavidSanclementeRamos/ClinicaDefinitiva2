# ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT

- **Código:** ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT
- **Nombre corto:** Documento inmutable
- **Mensaje base:** "No puede modificarse el número de documento una vez registrado"
- **Descripción clínica:**  
  El número de documento es un identificador crítico y, una vez creado el tercero, su modificación está prohibida para preservar la trazabilidad.  
  Cambiarlo podría romper referencias externas, auditorías y la unicidad por compañía, comprometiendo la integridad del sistema.
- **Operación / Caso de uso:** UPDATE_TERCERO
- **Regla de negocio:** RN-THIRDPARTIES-007 — Documento inmutable (ver ADR-18 — Simplificación de jerarquía de excepciones en el dominio).
- **Contexto del agregado:** USUARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 Conflict
- **Detalle dinámico sugerido:** "Intento de modificar documentNumber del tercero ID 123 (original=‘900123’, nuevo=‘800456’)"
- **Mapa a código existente:** Reemplaza InvalidThirdPartiesException para la modificación de documentNumber.
- **Justificación ética:** Protege la identidad del tercero y evita fraudes o confusiones operativas al impedir cambios en identificadores clave.
- **Ejemplo de uso:**
  ```java
  if (!Objects.equals(this.documentNumber, newDocumentNumber)) {
      throw new DomainException(ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** Al intentar setear un nuevo documentNumber distinto al original, debe lanzarse DomainException con ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT.
    - **Integración:** La API debe responder 409 Conflict y exponer el mensaje internacionalizable; verificar auditoría sin cambios en documentNumber.
- **Changelog / versión:**
    - 2025-12-09 — Autor: David — Alta inicial catálogo JournalEntry.

