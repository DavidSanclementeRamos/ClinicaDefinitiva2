## ERR_RECEPTIONIST_INVALID_LOCATION

- **Código:** ERR_RECEPTIONIST_INVALID_LOCATION  
- **Nombre corto:** Sede inválida recepcionista  
- **Mensaje base:** "El recepcionista debe estar asociado a una sede válida"  
- **Descripción clínica:**  
  Valida que todo recepcionista esté vinculado a una sede clínica válida. Evita registros inconsistentes y asegura que las operaciones administrativas estén correctamente asociadas a una ubicación física.  
- **Operación / Caso de uso:** REGISTRAR_RECEPCIONISTA (registerReceptionist)  
- **Regla de negocio:** RN-RECEPTIONIST-004 — Asociación obligatoria a sede válida (ver ADR-23)  
- **Contexto del agregado:** RECEPCIONISTA  
- **Tipo semántico:** Integridad  
- **Severidad sugerida:** ERROR  
- **HTTP sugerido:** 400  
- **Detalle dinámico sugerido:** "Recepcionista ID 23 registrado sin sede válida"  
- **Mapa a código existente:** Nuevo código  
- **Justificación ética:** Garantiza que los pacientes reciban atención en sedes verificadas y evita fraudes administrativos.  
- **Ejemplo de uso:**  
  ```java
  if (!clinicLocationRegistry.isValid(receptionist.location)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RECEPTIONIST_INVALID_LOCATION);
  }
  ```  
- **Pruebas mínimas requeridas:**  
  - **Unitario:** sede inválida → excepción.  
  - **Integración:** POST /receptionists con sede inexistente → 400.  
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Receptionist.  

---