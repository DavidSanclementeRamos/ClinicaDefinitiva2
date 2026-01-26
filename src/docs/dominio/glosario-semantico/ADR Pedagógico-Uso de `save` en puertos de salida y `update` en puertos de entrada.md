# ADR Pedagógico: Uso de `save` en puertos de salida y `update` en puertos de entrada

**Fecha:** 26-01-2026  
**Estado:** Aprobado  
**Decisión:** Mantener en los puertos de salida (repositorios) únicamente métodos genéricos como `save`, mientras que en los puertos de entrada (casos de uso) se definen métodos específicos de actualización según las reglas de negocio.

---

## Contexto
En la arquitectura clínica se observó que:
- Los **puertos de salida** (repositorios) solo exponen métodos genéricos como `save`, `findById`, `findAll`, `deleteById`.
- Los **puertos de entrada** (UseCase) sí definen métodos específicos de actualización, como:
    - `updateContactData(...)`
    - `updateSensitiveData(...)`
    - `updateAvailability(...)` (solo en el caso de Dentists).

Esto puede parecer extraño al inicio: ¿por qué no tener también `updateContact` o `updateSensitive` en el repositorio?

---

## Decisión
- **Puertos de salida (repositorios):**
    - Solo deben exponer operaciones genéricas de persistencia (`save`, `delete`, `find`).
    - El método `save` ya cubre tanto inserciones como actualizaciones.
    - No deben conocer reglas de negocio ni tipos de actualización específicos.

- **Puertos de entrada (UseCase):**
    - Deben expresar las operaciones de negocio con nombres semánticos.
    - Por eso se definen métodos como `updateContactData`, `updateSensitiveData`, etc.
    - Estos métodos aplican la lógica de negocio y luego llaman al repositorio con `save`.

---

## Motivo del cambio
- **Separación de responsabilidades:**
    - El repositorio solo persiste datos.
    - El caso de uso decide qué campos se actualizan y cómo.

- **Desacoplamiento:**
    - La infraestructura no necesita conocer reglas de negocio.
    - Los cambios en la lógica de negocio no afectan la capa de persistencia.

- **Claridad semántica:**
    - Los casos de uso expresan operaciones de negocio con nombres claros.
    - Los repositorios mantienen una interfaz simple y genérica.

---

## Beneficios
- **Pedagógico:** ayuda a nuevos desarrolladores a entender dónde va cada responsabilidad.
- **Consistencia:** todos los repositorios tienen interfaces simples y uniformes.
- **Flexibilidad:** se pueden agregar nuevos tipos de actualización en los casos de uso sin tocar la infraestructura.
- **Mantenibilidad:** menos acoplamiento entre capas.

---

## Ejemplo práctico

### Puerto de entrada (UseCase)
```java
public interface DentistUseCase {
    DentistDto updateContactData(UpdateContactDto dto, Long id);
    DentistDto updateSensitiveData(UpdateSensitiveDto dto, Long id);
    DentistDto updateStatus(UpdateStatusDto dto, Long id);
}
```
### Puerto de salida (Repositorio)
```java
public interface DentistRepository {
Optional<DentistEntity> findById(Long id);
Page<DentistEntity> findAll(Pageable pageable);
DentistEntity save(DentistEntity entity);
void deleteById(Long id);
}
```

### Implementación del caso de uso
```java
@Override
public DentistDto updateContactData(UpdateContactDto dto, Long id) {
    DentistEntity entity = repository.findById(id).orElseThrow(...);
    entity.updateContact(dto); // lógica de negocio
    return mapper.toDto(repository.save(entity));
}
```
## Decisión arquitectónica
Este ADR establece como estándar que:

Los puertos de salida solo deben tener métodos genéricos (save, find, delete).

Los puertos de entrada deben definir métodos específicos de negocio (updateContactData, updateSensitiveData, etc.).

La lógica de negocio se resuelve en los casos de uso, y la infraestructura solo persiste entidades.

De esta forma, la arquitectura mantiene claridad, separación de responsabilidades y facilita el aprendizaje de nuevos miembros del equipo.