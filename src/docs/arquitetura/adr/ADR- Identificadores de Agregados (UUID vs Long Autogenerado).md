# ADR: Identificadores de Agregados (UUID vs Long Autogenerado)

**Fecha:** 17-01-2026  
**Estado:** Aceptado

---

## Contexto
En el inicio de la migración hacia arquitectura hexagonal, se planteó la idea de usar **UUID** como identificadores únicos para los agregados.  
Sin embargo, por desconocimiento de su funcionamiento y complejidad en la persistencia, se optó por delegar la generación de identificadores al motor de base de datos mediante **Long autogenerado**.  
Esto generó dudas sobre la utilidad de los **Value Objects (VO)** para IDs y sobre cómo deben interactuar las interfaces de entrada y salida.

---

## Decisión
Se establece como estándar arquitectónico:

- Mantener los **Value Objects (VO)** para IDs independientemente de la estrategia de generación (UUID o Long).
- El VO sigue siendo útil porque:
    - Aporta **semántica fuerte**: `PatientId ≠ InvoiceId`, aunque ambos sean Long.
    - Permite **validaciones** (no null, formato, invariantes).
    - Evita el uso de tipos primitivos crudos en el dominio.
- La estrategia de generación será **Long autogenerado por la base de datos**, dado que el sistema es monolítico y centralizado.
- El VO encapsulará el Long y se mantendrá la validación de null.

---

## Ejemplo

```java
// VO
public record PatientId(Long value) {
    public PatientId {
        if (value == null) throw new IllegalArgumentException("PatientId cannot be null");
    }
}

// Dominio
public class Patient {
    private final PatientId id;
    private final Name name;
    // ...
}

// Output Port
public interface PatientRepository {
    void save(Patient patient);
    Optional<Patient> findById(PatientId id);
}

// Infra Adapter
@Repository
public class JpaPatientRepository implements PatientRepository {
    @Override
    public void save(Patient patient) {
        PatientEntity entity = new PatientEntity();
        entity.setId(patient.getId().value()); // Long
        // ...
    }
}
```

## Consecuencias
### Positivas
- Claridad semántica y seguridad de tipos.

- Simplicidad en persistencia con Long autogenerado.

- Flexibilidad futura: se puede migrar a UUID sin romper el dominio.

### Negativas
- Dependencia del motor de BD para generación de IDs.

- No se pueden crear agregados offline sin conexión a BD.

## Estado arquitectónico
El VO para IDs no pierde utilidad aunque el tipo subyacente sea Long.
La diferencia está en la estrategia de generación, no en la semántica del dominio.
Esta decisión arquitectónica asegura consistencia y claridad en el manejo de identificadores, y permite una futura migración a UUID sin afectar el dominio.

