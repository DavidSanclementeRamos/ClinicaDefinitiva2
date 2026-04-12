

# ADR-06 (Actores): Validación de Guardian en el agregado Patient

- Estado: Aprobado
- Fecha: 2025-10-07
- Autor: David Stiven Sanclemente

## Contexto
En el dominio clínico, un paciente menor de edad requiere un responsable (Guardian) para poder ser registrado en el sistema.  
El agregado Patient contiene la edad y la relación con un Guardian. La regla de negocio es:

- Si el paciente es menor de edad → debe tener un Guardian asignado.
- Si no cumple esta condición → se lanza una excepción ResponsableNoAsignadoException.

El problema surge al implementar el método validarResponsable(), ya que depende de la instancia (this).  
El método de fábrica register() fue diseñado como static, lo que impide invocar directamente validaciones de instancia.

## Decisiones consideradas
1. Validar después de construir en el método estático register
   - ✅ Centraliza validación.
   - ❌ El Patient puede existir momentáneamente inválido.

2. Validar dentro del constructor o builder
   - ✅ Garantiza consistencia desde el nacimiento.
   - ✅ Semánticamente fuerte y alineado con DDD.
   - ❌ Menos flexible para escenarios especiales.

3. Delegar la validación a un servicio de dominio orquestador
   - ✅ Útil si dependiera de múltiples agregados.
   - ❌ Innecesario en este caso, menos exhibible.

4. Mover la lógica al Guardian
   - ✅ Ilegítimo: el guardian no conoce la edad del paciente.
   - ❌ Rompe semántica y trazabilidad.

## Decisión
Se elige la Opción 2: Validar dentro del constructor o builder.

## Justificación
- El Patient nunca puede existir en un estado inválido.
- Refuerza la consistencia del agregado como unidad de invariantes.
- Alineado con arquitectura hexagonal: el agregado es autosuficiente y legítimo.
- Permite documentar claramente la regla de negocio en un solo lugar exhibible (Patient).

## Consecuencias
- Todos los Patient creados serán válidos desde su nacimiento.
- Se simplifica la orquestación: los servicios de dominio no necesitan validar reglas que pertenecen al agregado.
- Se refuerza la trazabilidad ética: la validación queda documentada y exhibida en el ciclo de vida del paciente.

## Plan de implementación
1. Refactorizar constructor de Patient para incluir validarResponsable().
2. Implementar ResponsableNoAsignadoException en domain.patient.exceptions.
3. Documentar regla en docs/dominio/reglas-de-negocio/patient.md.
4. Añadir pruebas unitarias para escenarios:
   - Paciente menor sin guardian → excepción.
   - Paciente menor con guardian → válido.
   - Paciente mayor sin guardian → válido.

## Ejemplo
```java
private Patient(PersonRegistrationData data, UserModel user, Guardian guardian) {
this.age = data.getAge();
this.user = user;
this.guardian = guardian;

    validarResponsable();
}

private void validarResponsable() {
if (this.age < 18 && this.guardian == null) {
throw new ResponsableNoAsignadoException("Paciente menor sin responsable asignado");
}
}
```

## Relación con otros ADR
- [ADR-(Actores)-05-Representación TypeGuardian Vo híbrido.md](ADR-%28Actores%29-05-Representaci%C3%B3n%20TypeGuardian%20Vo%20h%C3%ADbrido.md)
- [ADR-(Dominio)-01-Implementación de Value-Objects.md](../ADR-%28Dominio%29-01-Implementaci%C3%B3n%20de%20Value-Objects.md)