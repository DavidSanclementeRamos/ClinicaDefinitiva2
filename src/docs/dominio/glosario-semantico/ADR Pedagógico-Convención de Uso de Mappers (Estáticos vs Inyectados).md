# ADR Pedagógico: Convención de Uso de Mappers (Estáticos vs Inyectados)

**Fecha:** 17-01-2026  
**Estado:** Aceptado como guía pedagógica

---

## Contexto
En la arquitectura hexagonal, los *adapters* requieren mapeo entre entidades de infraestructura, DTOs y agregados del dominio.  
Surge la duda sobre si los mappers deben implementarse como **métodos estáticos** o como **clases inyectadas** (atributos finales en el adapter).  
Esta decisión afecta la claridad, extensibilidad y testabilidad del sistema.

---

## Opciones

### Opción 1: Métodos estáticos
**Ventajas:**
- Simples de invocar: `PatientMapper.toEntity(patient)`.
- No requieren inyección de dependencias.
- Fáciles de testear en aislamiento.

**Desventajas:**
- Difícil extender o reemplazar.
- Menos flexibles si el mapeo crece o requiere composición.

---

### Opción 2: Mapper inyectado (atributo final)
**Ventajas:**
- Flexibilidad: se puede cambiar la implementación sin modificar el adapter.
- Testabilidad: permite mockear el mapper en pruebas.
- Escalabilidad: soporta composición de mappers (ej. `ContactInfoMapper` dentro de `ActorMapper`).

**Desventajas:**
- Más código (constructores, wiring en DI).
- Puede parecer redundante si el mapeo es trivial.

---

## Decisión
Adoptar una **convención híbrida**:

- Usar **métodos estáticos** para mappers simples que solo convierten atributos básicos.
- Usar **mappers inyectados** para casos complejos con múltiples *Value Objects*, composición de mappers o lógica adicional.

---

## Ejemplo

```java
// Mapper estático
public class NameMapper {
    public static String toEntity(Name name) {
        return name.value();
    }
    public static Name toDomain(String value) {
        return new Name(value);
    }
}

// Mapper inyectable
@Component
public class ActorMapper {
    private final ContactInfoMapper contactInfoMapper;

    public ActorMapper(ContactInfoMapper contactInfoMapper) {
        this.contactInfoMapper = contactInfoMapper;
    }

    public ActorEntity toEntity(Actor actor) {
        ActorEntity entity = new ActorEntity();
        entity.setId(actor.getId().value());
        entity.setContactInfo(contactInfoMapper.toEntity(actor.getContactInfo()));
        return entity;
    }

    public Actor toDomain(ActorEntity entity) {
        return new Actor(
            new ActorId(entity.getId()),
            contactInfoMapper.toDomain(entity.getContactInfo())
        );
    }
}
```

## Consecuencias
### Positivas
- Claridad en cuándo usar cada enfoque.

- Flexibilidad para crecer sin comprometer simplicidad inicial.

- Mejor testabilidad en adapters complejos.

### Negativas
- Requiere disciplina para aplicar la convención.

- Posible mezcla de estilos en el código si no se documenta bien.

## Aprendizaje
La complejidad del dominio (muchos Value Objects) justifica el uso de mappers inyectados en adapters.
Los métodos estáticos siguen siendo útiles en casos simples.
Documentar esta convención evita debates futuros y asegura consistencia en el proyecto.