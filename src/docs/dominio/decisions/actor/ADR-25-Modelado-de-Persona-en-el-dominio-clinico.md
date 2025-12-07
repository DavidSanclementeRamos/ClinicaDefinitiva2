# ADR-25 (Dominio): Modelado de Persona en el dominio clínico

- Estado: Aprobado
- Fecha: 2025-10-24
- Autor: David

## Contexto
En el dominio clínico-administrativo existen entidades que comparten atributos comunes (nombre, documento, contacto), como Paciente, Odontólogo y Responsable.  
Una opción inicial fue modelarlas mediante herencia a partir de una clase base Persona. Sin embargo, esto genera problemas:
- Todos heredarían el mismo id, lo cual contradice la necesidad de que cada agregado raíz tenga su propia identidad (PatientId, DentistId, ResponsibleId).
- La herencia introduce acoplamiento innecesario y reduce la expresividad semántica: un Paciente no debería ser intercambiable con un Odontólogo.
- En arquitectura hexagonal, los agregados deben ser autónomos y referenciados solo por su identidad.

## Decisión
Se descarta la herencia de una clase Persona.  
En su lugar, se utilizará un Value Object PersonInfo que encapsule los atributos comunes (nombre, documento, contacto, etc.).  
Cada agregado raíz (Paciente, Odontólogo, Responsable) tendrá su propio ID VO y contendrá un PersonInfo para los datos compartidos.

## Ejemplo
```java
public final class PersonInfo {
private final String fullName;
private final String documentNumber;
private final String email;
}

public final class Patient {
private final PatientId id;
private final PersonInfo info;
// atributos específicos de paciente
}

public final class Dentist {
private final DentistId id;
private final PersonInfo info;
// atributos específicos de odontólogo
}
```

En agregados que necesiten referenciar a estas entidades (ej. Factura), se usará únicamente el ID VO (PatientId, DentistId, ResponsibleId).

## Consecuencias
-  Cada agregado raíz mantiene su propia identidad y autonomía.
-  Se evita acoplamiento innecesario y se gana expresividad semántica.
-  Se facilita la trazabilidad y la integración con sistemas externos.
-  Se requiere un paso adicional para recuperar datos de la otra entidad (repositorio/servicio de aplicación).
-  Puede percibirse como más “verboso” que la herencia, pero ofrece mayor claridad y robustez a largo plazo.

## Plan de implementación
1. Crear VO PersonInfo con atributos comunes.
2. Integrar PersonInfo en Patient, Dentist, Responsible.
3. Definir IDs específicos (PatientId, DentistId, ResponsibleId).
4. Ajustar repositorios para recuperar entidades completas cuando se requiera.
5. Documentar en docs/dominio/personas.md la separación entre identidad y datos compartidos.

## Relación con otros ADR
- ADR-23 (Dominio): Desactivación de usuario debe ser responsabilidad del agregado UserModel.
- ADR-11 (Dominio): Uso meticuloso de excepciones personalizadas.
- ADR-20 (Dominio): Inquietud sobre el rol de los Servicios de Dominio frente a métodos en Agregados.