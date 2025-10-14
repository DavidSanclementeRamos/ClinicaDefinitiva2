# ADR: Alcance experimental y exclusión temporal de validaciones específicas de recursos

## Contexto
El proyecto es de naturaleza experimental, no responde a un requerimiento cliente ni es un software a medida para una clínica concreta. Algunas reglas de negocio reales relevantes para agendamiento son dependientes de datos específicos de cada organización, por ejemplo: disponibilidad y escasez de equipos quirúrgicos, disponibilidad de salas, políticas internas de uso de recursos y mantenimiento programado.  
Recrear esas reglas y datos en el contexto experimental implicaría invertir tiempo significativo en modelado, datos de prueba y validaciones que no aportan valor al objetivo actual del proyecto y podrían introducir complejidad prematura.

## Decisión
Limitar el alcance del dominio de agendamiento a reglas generales y universales que no requieran datos específicos de una clínica. Excluir temporalmente del core del proyecto validaciones que dependen de la escasez o gestión concreta de recursos (equipos, salas, personal adicional), dejando preparado el diseño para permitir su incorporación futura si surge un cliente con requisitos concretos.

## Especificaciones de la decisión:
- Mantener en el dominio las invariantes generales como validez de intervalos, cobertura por slots de disponibilidad declarada, y prevención de solapamientos entre citas Scheduled.
- No modelar por ahora agregados o entidades persistentes para equipos ni agendas de recursos específicos salvo como Value Objects ligeros o stubs no persistidos para pruebas mínimas.
- Diseñar la arquitectura y las API internas de forma escalable para añadir más agregados y validaciones transversales (ej. EquipmentSchedule, RoomSchedule, servicios de dominio) sin refactorización disruptiva.

## Motivación
- Evitar desperdiciar tiempo en reglas que requieren datos reales y decisiones de negocio que no pueden deducirse correctamente sin información del cliente.
- Priorizar creación de un núcleo robusto y exhibible que valide las invariantes del dominio independiente del contexto operativo específico.
- Mantener la capacidad de extensión rápida hacia validaciones de recursos cuando un cliente real o caso de uso demanden esa funcionalidad.

## Consecuencias

Positivas
- Desarrollo más rápido y foco en lo esencial: invariantes del dominio claramente auditables y exhibibles.
- Menor complejidad temprana, pruebas más simples y menor riesgo de diseño prematuro.
- Arquitectura preparada para extender con agregados de recursos y servicios de dominio cuando sea necesario.

Negativas
- El sistema no cubrirá escenarios reales donde la disponibilidad de equipos o salas sea un factor crítico; esas limitaciones deben dejarse explícitas en documentación y demos.
- Posible necesidad de trabajo adicional al integrar requisitos reales de cliente, aunque mitigada por la decisión de diseñar para extensibilidad.

## Estado
- Fecha: 2025-10-8
- Aceptado. Aplicable a la fase experimental y de prototipado del proyecto. Revisable cuando exista interés de un cliente real o señales claras de adopción que justifiquen inversión en modelado y datos específicos.

Próximos pasos
- Documentar en la README y en ADRs relacionados que las validaciones de recursos concretos están fuera de alcance experimental y enumerar ejemplos de lo que se excluye.
- Implementar stubs o interfaces mínimas para EquipmentSchedule y RoomSchedule que permitan integrarlas posteriormente sin romper APIs públicas.
- Preparar pruebas de integración que simulen la incorporación futura de validaciones de recursos para validar la estrategia de extensibilidad.

Opinión profesional
Tu decisión es sólida y alineada con buenas prácticas de diseño evolutivo. En fases experimentales es preferible invertir en un núcleo semánticamente correcto y exhibible antes que modelar todas las variantes contextuales posibles. Modelado prematuro de recursos específicos suele generar deuda técnica y coste de mantenimiento cuando se carece de datos reales y reglas acordadas con el dominio. Mantener el foco en invariante