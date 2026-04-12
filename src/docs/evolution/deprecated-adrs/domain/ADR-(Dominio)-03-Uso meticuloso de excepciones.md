
# ADR-03 (Dominio): Uso meticuloso de excepciones personalizadas

- Estado: Parcialmente superado ADR 18 Y ADR 52
- Fecha: 2025-10-05
- Autor: David Stiven Sanclemente

## Contexto
En el diseño del sistema clínico, cada Value Object (VO) encapsula reglas de legitimidad que deben ser trazables, exhibibles y éticamente justificadas.  
Las excepciones no son simples mecanismos de control de flujo, sino artefactos semánticos que:
- Representan la violación de una regla explícita del dominio.
- Permiten auditar el origen, la intención y el contexto de cada fallo.
- Refuerzan la trazabilidad entre entidades, reglas clínicas y decisiones arquitectónicas.

## Decisión
Se ha decidido utilizar excepciones personalizadas de forma meticulosa, separando los tipos de violaciones según su naturaleza:
- Ausencia técnica (null) vs. ausencia semántica (blank, empty).
- Violaciones de catálogo vs. violaciones de cardinalidad.
- Errores de VO autónomos vs. errores orquestados por entidades.

## Cada excepción incluye:
- Un nombre exhibible y semántico (EmptySpecialtySetException, InvalidSpecialtyValueException).
- Un ContextoEntidad que declara quién legitima la regla.
- Un mensaje ético internacionalizable y auditable.
- Documentación que vincula la excepción con su regla, su ADR y su catálogo de errores.

## Justificación
- Trazabilidad ética: cada excepción declara su origen y legitimidad.
- Exhibición semántica: nombres y mensajes comprensibles para auditores, clínicos y desarrolladores.
- Reparación contextual: diferenciar null de blank permite protocolos de reparación distintos.
- Internacionalización: mensajes adaptables a distintas culturas y jurisdicciones.
- Auditoría técnica: registro preciso de qué regla fue violada y por quién.

## Alternativas consideradas
- Usar una sola excepción genérica (IllegalArgumentException): descartado por falta de trazabilidad y exhibición.
- Unificar errores técnicos y semánticos: descartado por ambigüedad en reparación y legitimidad.

## Implicaciones
- Se requiere mantener un catálogo de excepciones vinculado a reglas y entidades.
- Las excepciones deben documentarse con su ContextoEntidad, mensaje ético y trazabilidad.
- Los desarrolladores deben seguir una convención clara para crear nuevas excepciones.

## Convenciones para creación de excepciones
Crear una excepción nueva cuando:
- La regla violada tiene semántica distinta.
- El ContextoEntidad cambia.
- El mensaje ético requiere internacionalización específica.

Reutilizar excepciones cuando:
- La violación es idéntica en múltiples entidades.
- La semántica y el mensaje son universales.

## Plan de implementación
1. Definir plantilla base para excepciones personalizadas en domain-common/exceptions.
2. Crear excepciones concretas para VOs prioritarios (Specialty, FullName, Address).
3. Documentar cada excepción en docs/errors/CatalogoErrores.md con código, mensaje, ADR y contexto.
4. Refactorizar VOs para lanzar excepciones tipadas en lugar de genéricas.
5. Añadir pruebas unitarias para validar excepciones y sus metadatos.
6. Integrar con GlobalControllerAdvice para traducir excepciones a respuestas API.

## Ejemplo
```java
public class EmptySpecialtySetException extends ValueObjectValidationException {
public EmptySpecialtySetException() {
super("VOSPECIALTYEMPTY", "El conjunto de especialidades no puede estar vacío");
}
}

public final class SpecialtySet {
private final Set<Specialty> specialties;

    public SpecialtySet(Set<Specialty> specialties) {
        if (specialties == null || specialties.isEmpty()) {
            throw new EmptySpecialtySetException();
        }
        this.specialties = specialties;
    }
}
```

## Relación con otros ADR
- [ADR-(Arquitectura)-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../../architecture/decisions/arch/ADR-%28Arquitectura%29-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)
- [ADR-(Arquitectura)-52-Jerarquía definitiva de excepciones de dominio y gobernanza de documentación de catálogos.md](../../../architecture/decisions/arch/ADR-%28Arquitectura%29-52-Jerarqu%C3%ADa%20definitiva%20de%20excepciones%20de%20dominio%20y%20gobernanza%20de%20documentaci%C3%B3n%20de%20cat%C3%A1logos.md)