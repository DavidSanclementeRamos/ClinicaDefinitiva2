## ERR_SERVICE_DESCRIPTION_INVALID

- **Código:** ERR_SERVICE_DESCRIPTION_INVALID
- **Nombre corto:** Descripción inválida
- **Mensaje base:** "La descripción debe tener al menos 10 caracteres y no puede ser nula"
- **Descripción:**  
  Evita descripciones insuficientes que impidan comprender el alcance del servicio, sus limitaciones y requisitos.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_SERVICIO (createOrUpdateService)
- **Regla de negocio:** RN-SERV-001 — Validación de descripción (ver ADR-121)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Validación de contenido
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "description recibido: 'Revisión' longitud 8"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura información suficiente para consentimiento y expectativas realistas.
- **Ejemplo de uso:**
  ```java
  if (service.getDescription() == null || service.getDescription().trim().length() < 10) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DESCRIPTION_INVALID);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** description="Revisión" → excepción.
    - **Integración:** POST /services con description corto → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---
