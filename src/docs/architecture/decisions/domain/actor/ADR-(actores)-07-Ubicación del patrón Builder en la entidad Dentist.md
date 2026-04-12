# ADR-07 (Actores): Ubicación del patrón Builder en la entidad Dentist

- *Estado:* Aprobado
- *Fecha:* 2025-10-10
- *Autor:* David Stiven Sanclemente

## Contexto
La entidad clínica Dentist requiere un mecanismo flexible para construir instancias válidas, respetando sus invariantes y evitando el uso de constructores con múltiples parámetros o valores nulos.  
Para ello, se implementó el patrón *Builder*, inicialmente como una clase interna dentro de Dentist.

## Problema
Surge la duda sobre si el Builder debería vivir dentro de la entidad (como clase interna) o como una clase externa desacoplada.  
Esta decisión afecta la cohesión del modelo, el acoplamiento entre construcción y dominio, la testabilidad y la capacidad de evolución del sistema.

## Decisión
Se mantiene el Builder como clase interna dentro de Dentist, dado que:
- La construcción de Dentist se realiza exclusivamente dentro del dominio clínico.
- El Builder accede a campos privados sin necesidad de setters ni exposición innecesaria.
- Las invariantes del agregado se validan en el momento de construcción, sin depender de infraestructura ni de otros agregados.
- No se requiere construir Dentist desde adaptadores externos (REST, CLI, migraciones) en esta etapa del proyecto.

## Justificación
- *Cohesión semántica:* el Builder forma parte del ciclo de vida de la entidad y está alineado con sus reglas internas.
- *Encapsulamiento:* evita exponer constructores públicos o campos mutables.
- *Simplicidad:* reduce la complejidad del modelo al mantener la construcción dentro del mismo contexto.
- *Legitimidad clínica:* el proceso de construcción refleja decisiones clínicas que deben validarse en el dominio, no en la infraestructura.

## Consecuencias
- El Builder permanece como clase interna de Dentist, accesible desde métodos de fábrica como register(...), update(...), etc.
- Si en el futuro se requiere construir Dentist desde adaptadores externos o procesos de migración, se podrá refactorizar el Builder como clase externa (DentistBuilder) sin romper el modelo actual.
- Se mantiene la trazabilidad de las decisiones clínicas dentro del agregado, facilitando la documentación ética y semántica del sistema.

## Plan de implementación
1. Definir clase interna Builder dentro de Dentist.
2. Implementar métodos encadenados (withName, withContact, withSpecialty, etc.).
3. Validar invariantes en el método build().
4. Exponer métodos de fábrica (register, update) que usen el Builder.
5. Documentar el patrón en docs/dominio/vo/DentistBuilder.md.
6. Añadir pruebas unitarias para construcción de instancias válidas e inválidas.

## Ejemplo
```java
Dentist dentist = Dentist.builder()
    .withName("Dr. Juan Pérez")
    .withPhone("3001234567")
    .withAddress("Calle 10 #20-30")
    .build();
```
