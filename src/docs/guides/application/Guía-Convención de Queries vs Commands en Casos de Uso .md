

# Guía: Convención de Queries vs Commands en Casos de Uso

**Última actualización:** 2026-02-02  
**Tipo:** Guía de implementación

---

## Regla nemotécnica

```
Query   = Preguntar → nunca falla, puede devolver vacío
Command = Ordenar  → puede fallar, lanza excepción
```

---

## Queries (Operaciones de lectura)

### Definición
Operaciones que **obtienen información** del sistema sin modificar el estado.

### Características
- Devuelven `Optional`, `Page`, `List` u otro contenedor seguro.
- **Nunca lanzan excepción** por ausencia de datos.
- Ubicadas en **Input Ports** de lectura.
- Implementadas por Application Services que usan Output Ports (repositorios).
- La capa superior (REST, CLI, etc.) decide cómo manejar el caso vacío (ej. devolver `404`).

### Ejemplo

```java
// Input Port
public interface FindRolUseCase {
    Optional<ReadRolDto> findById(Long id);
}

// Application Service
public class RolApplicationService implements FindRolUseCase {
    private final RolRepository repository;
    private final RolReadMapper mapper;

    @Override
    public Optional<ReadRolDto> findById(Long id) {
        return repository.findById(RolId.of(id))
                         .map(mapper::toReadDto);
    }
}
```

---

## Commands (Operaciones de escritura)

### Definición
Operaciones que **modifican el estado** del sistema.

### Características
- Devuelven DTOs o `void`.
- **Lanzan excepción** si la operación no puede completarse (ej. entidad no encontrada, regla de negocio violada).
- Ubicadas en **Input Ports** de escritura.
- Implementadas por Application Services que usan Output Ports (repositorios).

### Ejemplo

```java
// Input Port
public interface DeleteRolUseCase {
    void delete(Long id);
}

// Application Service
public class RolApplicationService implements DeleteRolUseCase {
    private final RolRepository repository;

    @Override
    public void delete(Long id) {
        Rol rol = repository.findById(RolId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Rol not found: " + id));
        rol.delete(); // lógica de agregado
        repository.save(rol);
    }
}
```

---

## Comparación visual

```
┌───────────────────────────────────────────────┐
│                 CASOS DE USO                  │
├───────────────────────────────────────────────┤
│ Query   → findById, findAll, findByUserId     │
│ Command → save, update, delete                │
└───────────────────────────────────────────────┘

Query   = devuelve Optional / Page / List
Command = lanza excepción si no puede completarse
```

---

## Tabla comparativa

| Aspecto         | Query (lectura)                  | Command (escritura)              |
|-----------------|----------------------------------|----------------------------------|
| **Propósito**   | Obtener información              | Modificar estado                 |
| **Resultado**   | `Optional`, `Page`, `List`       | DTO o `void`                     |
| **Ausencia**    | Devuelve vacío                   | Lanza excepción                  |
| **Ejemplo**     | `findById(Long id)`              | `delete(Long id)`                 |
| **Error**       | No aplica                        | `IllegalArgumentException`, `IllegalStateException` |

---

## Checklist de implementación

Al crear un nuevo caso de uso:

**Query:**
- [ ] Crear interfaz en `/application/port/in/`
- [ ] Devuelve `Optional`, `Page` o `List`
- [ ] Nunca lanza excepción por ausencia
- [ ] Application Service implementa la interfaz
- [ ] Controlador decide cómo manejar vacío (ej. 404)

**Command:**
- [ ] Crear interfaz en `/application/port/in/`
- [ ] Devuelve DTO o `void`
- [ ] Lanza excepción si no puede completarse
- [ ] Application Service implementa la interfaz
- [ ] Controlador traduce excepción a error HTTP (ej. 400/404)

---

## Resumen en una frase

**Query = ausencia es normal → Optional**  
**Command = ausencia es error → excepción**
