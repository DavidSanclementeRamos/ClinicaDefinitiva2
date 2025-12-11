

# ADR-Eliminación del patrón Builder en Receptionist

- **Estado:** Aprobado
- **Fecha:** 2025-12-10
- **Autor:** David

## Contexto
En [ADR-008-Ubicación-del-patrón-Builder-en-la-entidad-Dentist.md](ADR-008-Ubicaci%C3%B3n-del-patr%C3%B3n-Builder-en-la-entidad-Dentist.md) se decidió que los agregados clínicos utilizarían el patrón *Builder* para garantizar construcción flexible y validación de invariantes.  
En  [ADR-00-Formas-de-crear-un-objecto.md](../../glosario-semantico/ADR-00-Formas-de-crear-un-objecto.md) se exploraron las formas de construcción de objetos (Builder vs Setters vs Atributos directos), concluyendo que el Builder sería la opción preferida en agregados ricos.

Sin embargo, durante la implementación del agregado **Receptionist** se identificó que su ciclo de vida y reglas de negocio no requieren la flexibilidad del Builder. La creación de un Receptionist depende de un conjunto fijo de atributos obligatorios (`ReceptionId`, `Person`, `UserIdentity`, `Sector`) y de reglas de negocio que se expresan mejor en un método de fábrica (`registerReceptionist`).

## Problema
Mantener un Builder en Receptionist introduce complejidad innecesaria:
- Permite estados intermedios inválidos (ej. Receptionist sin `Person` o sin `Sector`).
- Duplica la lógica de construcción que ya está cubierta por el método de fábrica.
- Rompe la claridad semántica: la intención de negocio es “registrar recepcionista”, no “construir recepcionista paso a paso”.

## Decisión
Se elimina el Builder en Receptionist y se reemplaza por un **constructor privado + método de fábrica estático** (`registerReceptionist`).
- La creación de Receptionist se realiza exclusivamente mediante el método de fábrica, garantizando invariantes desde el inicio.
- Las actualizaciones se realizan mediante métodos de negocio (`updateContactData`, `updateSensitiveData`) y no mediante setters públicos.
- Las reglas que cruzan agregados (ej. validación de usuario activo) se delegan a un **Domain Service** (`ReceptionistOrchestratorService`).

## Justificación
- **Protección de invariantes:** Receptionist siempre nace válido, sin estados parciales.
- **Claridad semántica:** el método `registerReceptionist` refleja directamente la intención de negocio.
- **Simplicidad:** se reduce la verbosidad y se evita sobre-ingeniería en un agregado con construcción fija.
- **Consistencia con  [ADR-00-Formas-de-crear-un-objecto.md](../../glosario-semantico/ADR-00-Formas-de-crear-un-objecto.md)
  :** Receptionist se clasifica como un agregado simple en cuanto a construcción, por lo que no requiere Builder.

## Consecuencias
- Receptionist se construye únicamente mediante `registerReceptionist`.
- Se elimina la clase interna Builder y sus métodos encadenados.
- Se mantiene la coherencia con [ADR-008-Ubicación-del-patrón-Builder-en-la-entidad-Dentist.md](ADR-008-Ubicaci%C3%B3n-del-patr%C3%B3n-Builder-en-la-entidad-Dentist.md) y  [ADR-00-Formas-de-crear-un-objecto.md](../../glosario-semantico/ADR-00-Formas-de-crear-un-objecto.md)
  , documentando la excepción como caso especial.
- Exhibición más profesional del modelo, evitando complejidad innecesaria.


## Ejemplo
```java
Receptionist receptionist = Receptionist.registerReceptionist(
    ReceptionId.generate(),
    person,
    userIdentity,
    sector
);
```

## Relación con otros ADR
- [ADR-008-Ubicación-del-patrón-Builder-en-la-entidad-Dentist.md](ADR-008-Ubicaci%C3%B3n-del-patr%C3%B3n-Builder-en-la-entidad-Dentist.md)
- [ADR-00-Formas-de-crear-un-objecto.md](../../glosario-semantico/ADR-00-Formas-de-crear-un-objecto.md)
- ADR-18 (Dominio): Simplificación general de jerarquía de excepciones en el dominio.

---
