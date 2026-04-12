## ERR_SERVICE_NAME_CUSTOM_INVALID

- **Código:** ERR_SERVICE_NAME_CUSTOM_INVALID
- **Nombre corto:** Nombre personalizado inválido
- **Mensaje base:** "El nombre personalizado debe tener al menos 3 caracteres y no puede ser nulo"
- **Descripción:**  
  Evita nombres de servicio demasiado cortos o nulos que dificulten identificación, búsqueda y comunicación con pacientes y personal.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_SERVICIO (createOrUpdateService)
- **Regla de negocio:** RN-SERV-001 — Validación de nombre personalizado (ver ADR-120)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "customName recibido: '' longitud 0"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza claridad en la información entregada al paciente y evita confusiones administrativas.
- **Ejemplo de uso:**
  ```java
  if (service.getCustomName() == null || service.getCustomName().trim().length() < 3) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_NAME_CUSTOM_INVALID);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** customName=null → excepción.
    - **Integración:** POST /services con customName="ab" → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---
