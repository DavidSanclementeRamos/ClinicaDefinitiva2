## ERR_PROSTHETIC_INVALID_TYPE_VALUE

- **Código:** ERR_PROSTHETIC_INVALID_TYPE_VALUE
- **Nombre corto:** Valor de tipo inválido
- **Mensaje base:** "El tipo debe ser FIXED (fija) o REMOVABLE (removible)"
- **Descripción clínica:**  
  Asegura que el campo tipo solo acepte valores del dominio esperado, evitando ambigüedad en la clasificación de la prótesis y sus flujos asociados (laboratorio, materiales, procedimientos).
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_PRÓTESIS (createOrUpdateProsthesis)
- **Regla de negocio:** RN-PROSTHETIC-004 — Validación de valores permitidos para tipo (ver ADR-71)
- **Contexto del agregado:** PRÓTESIS_PROSTHÉTICA
- **Tipo semántico:** Integridad de catálogo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Tipo recibido: 'HYBRID' no permitido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita tratamientos mal categorizados que puedan llevar a errores en la técnica, materiales o consentimiento informado.
- **Ejemplo de uso:**
  ```java
  if (!"FIXED".equalsIgnoreCase(prosthesis.getType()) && !"REMOVABLE".equalsIgnoreCase(prosthesis.getType())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PROSTHETIC_INVALID_TYPE_VALUE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** type="HYBRID" → excepción.
    - **Integración:** POST /prosthetics con type="TEMPORARY" → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Prosthetic.

---