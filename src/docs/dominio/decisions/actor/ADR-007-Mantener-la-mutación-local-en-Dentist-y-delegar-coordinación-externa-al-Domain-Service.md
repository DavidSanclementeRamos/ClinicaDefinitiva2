# ADR-05 (Dominio): Mantener mutación local en Dentist y delegar coordinación externa al Domain Service

- **Estado:** Aprobado
- **Fecha:** 2025-10-05
- **Autor:** David

## Contexto
La entidad clínica `Dentist` expone un método `actualizarDatosDeContacto(...)` que permite modificar su dirección y número telefónico, validando previamente que el usuario asociado esté activo.  
Esta operación es semánticamente legítima: no accede a infraestructura ni a otros agregados, y preserva las invariantes del modelo.

En escenarios reales, actualizar los datos de contacto puede requerir operaciones adicionales como:
- Validar duplicidad de teléfono en otros actores clínicos.
- Registrar auditoría del cambio.
- Persistir la nueva instancia en el repositorio.
- Emitir eventos de dominio (`DentistContactUpdated`).
- Enviar notificaciones al profesional o al equipo administrativo.
- Verificar permisos del actor que solicita el cambio.

## Problema
Si se intenta implementar toda esta lógica dentro de la entidad `Dentist`, se rompe el principio de separación de responsabilidades, se acopla el modelo a infraestructura y se dificulta la testabilidad y evolución del sistema.

## Decisión
Se mantiene el método `actualizarDatosDeContacto(...)` dentro de la entidad `Dentist` como operación pura, semántica y validada internamente.  
Las operaciones externas, transaccionales y coordinadas se delegan a un **Domain Service** (`UpdateDentistContactService`), el cual:
- Recupera la entidad desde el repositorio.
- Valida permisos y reglas cruzadas.
- Invoca el método de la entidad para obtener la nueva instancia.
- Persiste el cambio.
- Registra auditoría y publica eventos.
- Dispara notificaciones si corresponde.

## Justificación
- **Claridad semántica:** la entidad expresa su intención y valida sus reglas internas.
- **Separación de capas:** el servicio coordina infraestructura, seguridad y efectos externos.
- **Testabilidad:** se pueden probar la entidad y el servicio por separado.
- **Evolución legítima:** si la operación se vuelve más compleja, el servicio puede crecer sin romper la entidad.
- **Arquitectura hexagonal:** el servicio actúa como puerto de entrada (caso de uso), mientras la entidad permanece como núcleo del dominio.

## Consecuencias
- El método `actualizarDatosDeContacto(...)` permanece en la entidad como operación legítima.
- El servicio de dominio orquesta el flujo completo de actualización.
- Se mejora la trazabilidad, la cohesión y la capacidad de evolución del sistema clínico.
- Se habilita la integración con adaptadores externos (REST, eventos, tareas) sin acoplarlos al modelo.

## Plan de implementación
1. Crear `UpdateDentistContactService` en `com.clinica.domain.service`.
2. Definir método `updateContact(DentistId id, ContactData data)`.
3. Refactorizar entidad `Dentist` para exponer solo la mutación semántica (`actualizarDatosDeContacto`).
4. Integrar con catálogo de errores clínicos (ver ADR-03).
5. Añadir pruebas unitarias para la entidad y el servicio.
6. Documentar operación en `docs/dominio/reglas-de-negocio/dentist.md`.

## Ejemplo
```java
Dentist dentist = dentistRepository.findById(dentistId);
dentist.actualizarDatosDeContacto(newContactData);
updateDentistContactService.orchestrate(dentist);

```

## Relación con otros ADR
- 	ADR-02 (Dominio): Implementación estratégica de Value Objects.
- 	ADR-03 (Arquitectura): Catálogo de errores clínicos por operación.
- 	ADR-04 (Dominio): Delegación de la lógica de desactivación de Dentist a un Domain Service.
- 	ADR-05 (Dominio): Consolidación semántica de horarios clínicos.
## Notas
Este patrón puede aplicarse a otras operaciones clínicas como desactivación, reasignación de turnos, actualización de especialidades o cambios contractuales.
Cada una puede tener su método semántico en la entidad y su servicio orquestador en la capa de aplicación.