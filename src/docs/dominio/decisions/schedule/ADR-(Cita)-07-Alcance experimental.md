# ADR-21 (Dominio): Alcance experimental y exclusión temporal de validaciones específicas de recursos

- Estado: Aceptado (fase experimental y prototipado)
- Fecha: 2025-10-08
- Autor: David

## Contexto
El proyecto es de naturaleza experimental, no responde a un requerimiento cliente ni es un software a medida para una clínica concreta.  
Algunas reglas de negocio reales relevantes para agendamiento dependen de datos específicos de cada organización (ej. disponibilidad de equipos quirúrgicos, salas, políticas internas de recursos).

Recrear esas reglas en el contexto experimental implicaría invertir tiempo significativo en modelado y pruebas sin aportar valor al objetivo actual, introduciendo complejidad prematura.

## Decisión
Limitar el alcance del dominio de agendamiento a reglas generales y universales que no requieran datos específicos de una clínica.  
Excluir temporalmente del core del proyecto validaciones que dependen de la escasez o gestión concreta de recursos (equipos, salas, personal adicional), dejando preparado el diseño para permitir su incorporación futura.

## Especificaciones
- Mantener en el dominio las invariantes generales:
    - Validez de intervalos.
    - Cobertura por slots de disponibilidad declarada.
    - Prevención de solapamientos entre citas Scheduled.
- No modelar por ahora agregados o entidades persistentes para equipos ni agendas de recursos específicos.
- Usar Value Objects ligeros o stubs no persistidos para pruebas mínimas.
- Diseñar arquitectura y APIs internas de forma escalable para añadir agregados y validaciones transversales (EquipmentSchedule, RoomSchedule, servicios de dominio) sin refactorización disruptiva.

## Motivación
- Evitar desperdicio de tiempo en reglas dependientes de datos reales.
- Priorizar un núcleo robusto y exhibible que valide invariantes universales.
- Mantener capacidad de extensión rápida hacia validaciones de recursos cuando un cliente real lo demande.

## Consecuencias
Positivas
- Desarrollo más rápido y foco en lo esencial.
- Menor complejidad temprana y pruebas más simples.
- Arquitectura preparada para extender con agregados de recursos.

Negativas
- El sistema no cubrirá escenarios reales donde la disponibilidad de equipos o salas sea crítica.
- Posible necesidad de trabajo adicional al integrar requisitos reales, mitigada por diseño extensible.

## Plan de implementación
1. Documentar en README y ADRs relacionados que las validaciones de recursos concretos están fuera de alcance experimental.
2. Implementar stubs mínimos para EquipmentSchedule y RoomSchedule.
3. Preparar pruebas de integración que simulen incorporación futura de validaciones de recursos.
4. Actualizar documentación en docs/dominio/reglas-de-negocio/resources.md.

## Ejemplo (stub)
```java
public class EquipmentScheduleStub {
    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        // Simulación mínima, siempre disponible en fase experimental
        return true;
    }
}
```

## Relación con otros ADR
- ADR-17 (Dominio): Validaciones de Reagendamiento de Citas.
- ADR-19 (Dominio): Revisión de uso de queries de Schedule en validaciones de reagendamiento.
- ADR-20 (Dominio): Inquietud sobre el rol de los Servicios de Dominio frente a métodos en Agregados.  
  

