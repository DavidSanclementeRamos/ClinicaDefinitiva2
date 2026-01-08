## ERR_SERVICE_DESCRIPTION_TOO_SHORT

- **Código:** ERR_SERVICE_DESCRIPTION_TOO_SHORT
- **Nombre corto:** Descripción demasiado corta
- **Mensaje base:** "La descripción debe tener al menos 20 caracteres"
- **Descripción:**  
  Asegura que la descripción del servicio sea suficientemente informativa para pacientes y personal, facilitando consentimiento y expectativas.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_SERVICIO (createOrUpdateService)
- **Regla de negocio:** RN-SERVICE-014 — Longitud mínima de descripción (ver ADR-119)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Validación de contenido
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "description length=12"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege al paciente al garantizar información mínima para la toma de decisiones.
- **Ejemplo de uso:**
  ```java
  if (service.getDescription().length() < 20) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DESCRIPTION_TOO_SHORT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** description length 10 → excepción.
    - **Integración:** POST /services con description corta → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---