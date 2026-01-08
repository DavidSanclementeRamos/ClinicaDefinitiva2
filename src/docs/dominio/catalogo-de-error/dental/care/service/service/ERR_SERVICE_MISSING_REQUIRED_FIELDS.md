## ERR_SERVICE_MISSING_REQUIRED_FIELDS

- **Código:** ERR_SERVICE_MISSING_REQUIRED_FIELDS
- **Nombre corto:** Campos obligatorios faltantes
- **Mensaje base:** "El nombre y descripción no pueden estar en blanco"
- **Descripción:**  
  Valida presencia de campos mínimos para publicar o activar un servicio en catálogo, garantizando información suficiente para pacientes y sistemas.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_SERVICIO (createOrUpdateService)
- **Regla de negocio:** RN-SERVICE-009 — Requerimiento de nombre y descripción (ver ADR-115)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "name='' description=''"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura que el paciente reciba información mínima para consentimiento y elección informada.
- **Ejemplo de uso:**
  ```java
  if (service.getName().isBlank() || service.getDescription().isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_MISSING_REQUIRED_FIELDS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** name="" → excepción.
    - **Integración:** POST /services con name o description vacíos → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---