

# ADR: Validación de Guardian(Responsable) en el agregado Patient(Paciente)

## Estado
- Aceptado
- Fecha: 2025-10-7
## Contexto
En el dominio clínico, un paciente menor de edad requiere un responsable (guardian) para poder ser registrado en el sistema.  
El agregado Patient contiene la edad y la relación con un Guardian. La regla de negocio es:

- Si el paciente es menor de edad → debe tener un Guardian asignado.
- Si no cumple esta condición → se lanza una excepción ResponsableNoAsignadoException.

El problema surge al implementar el método validarResponsable(), ya que este depende de la instancia (this).  
Sin embargo, el método de fábrica register() fue diseñado como static, lo que impide invocar directamente validaciones de instancia.

---

## Decisiones consideradas

1. Validar después de construir en el método estático register
   ```java
   public static Patient register(PersonRegistrationData data, UserModel user, Guardian guardian) {
       Patient patient = new Patient(data, user, guardian);
       patient.validarResponsable();
       return patient;
   }
   ```

- ✅ Mantiene el método de fábrica estático y centraliza la validación.
- ❌ El Patient puede existir momentáneamente en un estado inválido antes de la validación.
- ❌ Semánticamente débil: contradice la idea de que un agregado debe ser consistente desde su creación.

---

2. Validar dentro del constructor o builder
   ```java
   private Patient(PersonRegistrationData data, UserModel user, Guardian guardian) {
   this.age = data.getAge();
   this.user = user;
   this.guardian = guardian;

   validarResponsable(); // asegura consistencia al nacer
   }
   ```

- ✅ Garantiza que ningún Patient inválido pueda existir.
- ✅ Semánticamente fuerte: la consistencia del agregado se asegura desde el inicio.
- ✅ Compatible con arquitectura hexagonal: el agregado es autosuficiente y legítimo.
- ❌ Menos flexible si en el futuro se quiere construir un Patient incompleto para escenarios especiales (ej. importación de datos).

---

3. Delegar la validación a un servicio de dominio orquestador
   ```java
   public class PatientRegistrationService {
       public Patient register(PersonRegistrationData data, UserModel user, Guardian guardian) {
           Patient patient = Patient.register(data, user, guardian);
           patient.validarResponsable();
           return patient;
       }
   }
   ```

- ✅ Separa construcción y validación, útil si la regla dependiera de múltiples agregados.
- ❌ En este caso la regla depende solo de Patient (edad + guardian), por lo que usar un servicio es innecesario.
- ❌ Menos exhibible: la validación deja de estar en el lugar natural (el agregado).

---

4. Mover la lógica al Guardian
   ```java
   public boolean requiereResponsable() { ... }
   public boolean tieneResponsable() { ... }
   public void validarResponsable() { ... }
   ```

- ❌ Ilegítimo: el Guardian no conoce la edad del paciente ni puede decidir si él mismo es requerido.
- ❌ Rompe semántica: la necesidad de responsable es una regla del paciente, no del guardian.
- ❌ Pierde trazabilidad ética: el ciclo de vida del paciente queda fragmentado.

---

## Decisión
Se elige la Opción 2: Validar dentro del constructor o builder.

---

## Justificación
- Es la opción más semánticamente legítima: el Patient nunca puede existir en un estado inválido.
- Refuerza la consistencia del agregado como unidad de invariantes.
- Es la más alineada con arquitectura hexagonal, donde los agregados deben ser autosuficientes y consistentes.
- Permite documentar claramente la regla de negocio en un solo lugar exhibible (Patient).

---

## Consecuencias
- Todos los Patient creados serán válidos desde su nacimiento.
- Se simplifica la orquestación: los servicios de dominio no necesitan validar reglas que pertenecen al agregado.
- Se refuerza la trazabilidad ética: la validación queda documentada y exhibida en el ciclo de vida del paciente.  
  

---

