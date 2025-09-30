## ADR-007: Mantener la mutación local en Dentist y delegar coordinación externa al Domain Service

## Contexto:
La entidad clínica Dentist expone un método llamado actualizarDatosDeContacto(...) que permite modificar su dirección y número telefónico, validando previamente que el usuario asociado esté activo. Esta operación es semánticamente legítima, no accede a infraestructura ni a otros agregados, y preserva las invariantes del modelo.

Sin embargo, en escenarios reales, actualizar los datos de contacto puede requerir operaciones adicionales como:
- Validar duplicidad de teléfono en otros actores clínicos.
- Registrar auditoría del cambio.
- Persistir la nueva instancia en el repositorio.
- Emitir eventos de dominio (ej. DentistContactUpdated).
- Enviar notificaciones al profesional o al equipo administrativo.
- Verificar permisos del actor que solicita el cambio.

## Problema:
Si se intenta implementar toda esta lógica dentro de la entidad Dentist, se rompe el principio de separación de responsabilidades, se acopla el modelo a infraestructura, y se dificulta la testabilidad y evolución del sistema.

## Decisión:
Se mantiene el método actualizarDatosDeContacto(...) dentro de la entidad Dentist como una operación pura, semántica y validada internamente. Este método representa la intención clínica de modificar los datos de contacto, y preserva todas las invariantes del agregado.

Las operaciones externas, transaccionales y coordinadas se delegan a un Domain Service (ej. UpdateDentistContactService), el cual:
- Recupera la entidad desde el repositorio.
- Valida permisos y reglas cruzadas.
- Invoca el método de la entidad para obtener la nueva instancia.
- Persiste el cambio.
- Registra auditoría y publica eventos.
- Dispara notificaciones si corresponde.

## Justificación:
- *Claridad semántica*: la entidad expresa su intención y valida sus reglas internas.
- *Separación de capas*: el servicio coordina infraestructura, seguridad y efectos externos.
- *Testabilidad*: se pueden probar la entidad y el servicio por separado.
- *Evolución legítima*: si la operación se vuelve más compleja, el servicio puede crecer sin romper la entidad.
- *Arquitectura hexagonal*: el servicio actúa como puerto de entrada (caso de uso), mientras la entidad permanece como núcleo del dominio.

## Consecuencias:
- El método actualizarDatosDeContacto(...) permanece en la entidad como operación legítima.
- El servicio de dominio orquesta el flujo completo de actualización.
- Se mejora la trazabilidad, la cohesión y la capacidad de evolución del sistema clínico.
- Se habilita la integración con adaptadores externos (REST, eventos, tareas) sin acoplarlos al modelo.

## Notas:
Este patrón puede aplicarse a otras operaciones clínicas como desactivación, reasignación de turnos, actualización de especialidades, o cambios contractuales. Cada una puede tener su método semántico en la entidad y su servicio orquestador en la capa de aplicación.

Fin del documento.