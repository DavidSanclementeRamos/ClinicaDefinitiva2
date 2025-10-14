# ADR: Inquietud sobre el rol de los Servicios de Dominio frente a métodos en Agregados

## Contexto
Durante el diseño del método reagendar en la entidad Appointment, surgió la duda sobre la *utilidad real de los Servicios de Dominio*.  
La situación es la siguiente:
- Tanto el *Paciente* como el *Odontólogo* agrupan sus reglas de reagendamiento en métodos propios de sus agregados.
- El agregado *Schedule* también expone reglas para validar disponibilidad y solapamientos.
- Appointment.reagendar() actúa como *orquestador de reglas*, invocando a los agregados responsables antes de mutar su estado.
- Esto genera la inquietud:
  > *Si cada agregado valida lo suyo y Appointment coordina, ¿cuál es entonces la verdadera función de los Servicios de Dominio?*

## Decisión
Se reconoce que:
- *Es correcto* que cada agregado valide sus propias invariantes mediante métodos internos.
- *Es legítimo* que Appointment.reagendar() actúe como orquestador de esas validaciones, siempre que no absorba lógica que pertenece a otros agregados.
- Los *Servicios de Dominio* no son necesarios en este caso, porque la operación de reagendar se resuelve dentro de un conjunto de agregados relacionados (Paciente, Odontólogo, Agenda).
- Los *Servicios de Dominio* se reservarán para operaciones que:
    - Involucren *varios agregados raíz sin relación directa*.
    - Expresen reglas de negocio que no tienen un “hogar natural” en una entidad o agregado.
    - Requieran coordinación transversal (ej. agenda del odontólogo + sala de procedimientos + facturación).

## Consecuencias
### Positivas
- Se mantiene la *claridad semántica*: cada regla vive en su agregado legítimo.
- Appointment conserva un rol de *coordinador natural* de su propio ciclo de vida.
- Se evita crear servicios de dominio innecesarios, reduciendo complejidad artificial.

### Negativas
- Puede persistir la *confusión conceptual* sobre cuándo usar un servicio de dominio.
- Riesgo de que Appointment absorba demasiada orquestación si no se vigila la frontera entre coordinación y lógica de negocio transversal.

## Estado
- Fecha: 2025-10-8
- En revisión 🔄  
- Este ADR documenta la *inquietud y falta de claridad actual*, y se deja abierto para futura evolución a medida que surjan casos donde un servicio de dominio sea indispensable.

## Próximos pasos
- Documentar ejemplos concretos de *cuándo sí* y *cuándo no* usar un servicio de dominio.
- Revisar periódicamente si nuevas reglas de reagendamiento cruzan varios agregados raíz → en ese caso, refactorizar hacia un servicio de dominio.
- Mantener Appointment.reagendar() como orquestador legítimo, pero auditar que no absorba lógica ajena.