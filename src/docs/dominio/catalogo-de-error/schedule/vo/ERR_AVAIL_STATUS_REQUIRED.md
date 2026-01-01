## ERR_AVAIL_STATUS_REQUIRED

- **Código:** ERR_AVAIL_STATUS_REQUIRED
- **Nombre corto:** Estado de disponibilidad requerido
- **Mensaje base:** "El estado de Availability no puede ser nulo"
- **Descripción clínica:**  
  Impide que una disponibilidad exista sin un estado definido (ej. ACTIVE, INACTIVE, BLOCKED). Protege la coherencia operativa y evita comportamientos ambiguos en la agenda clínica.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-001 — Estado obligatorio en Availability
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Availability sin estado definido (valor recibido: null)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza decisiones claras sobre activación, bloqueo o desactivación, protegiendo la previsibilidad del servicio clínico.
- **Ejemplo de uso:**
  ```java
  if (availabilityStatus == null) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_AVAIL_STATUS_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** estado = null → excepción.
    - **Integración:** POST /availability → 400 si falta estado.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Availability.

---