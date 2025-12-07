# ADR-04 (Arquitectura): Jerarquía de excepciones para valores faltantes en Value Objects

- Estado: Aprobado
- Fecha: 2025-10-03
- Decidido por: David

## Contexto
Se requiere una convención clara, trazable y reutilizable para las excepciones que representan la ausencia de un valor con significado semántico en Value Objects (VOs) distribuidos por dominios (ej. person, schedule, clinical).  
Estas excepciones deben facilitar:
1. Manejo por semántica (todos los “missing semantic value”).
2. Manejo por contexto/paquete (errores pertenecientes a un dominio).
3. Auditoría/internacionalización con metadatos consistentes.

## Decisión
Adoptar una jerarquía consistente por paquete que combina un nombre semántico unido (MissingSemanticValueException) con subcategorías intermedias para distinguir null y blank.  
Cada paquete declara su propia MissingSemanticValueException que hereda de la excepción contextual del dominio.

## Estructura general por paquete
`
ValueObjectValidationException
├── Shared<Domain>Exception
│   └── MissingSemanticValueException
│       ├── NullValueException
│       │   └── Null<Field>NameException
│       ├── BlankValueException
│       │   └── Blank<Field>NameException
│       └── (futuras subcategorías p.ej. InvalidFormatException)
`

## Ejemplo concreto (domain.person.vo.exception)
`
ValueObjectValidationException
└── SharedPersonException
    └── MissingSemanticValueException
        ├── NullValueException
        │   └── NullAddressFieldException
        └── BlankValueException
            └── BlankAddressFieldException
`

En domain.schedule.vo.exception se mantiene la misma forma, pero la MissingSemanticValueException extiende SharedScheduleException.

## Justificación
- Consistencia terminológica: el nombre MissingSemanticValueException transmite la misma intención en todos los dominios.
- Contextualización por paquete: preserva separación de responsabilidades y trazabilidad del origen.
- Distinción técnica y semántica: subcategorías NullValueException y BlankValueException permiten mensajes y tratamiento operativo distintos.
- Manejo flexible: capas internas capturan MissingSemanticValueException; capas superiores capturan excepciones contextuales (SharedPersonException).
- Auditoría e I18n: uniformidad del nombre facilita catálogos y claves de internacionalización coherentes.

## Implementación recomendada

Plantilla base
```java
public abstract class MissingSemanticValueException extends SharedXException {
private final String fieldName;
private final Object receivedValue;
private final String i18nKey;

    public MissingSemanticValueException(String fieldName, Object receivedValue, String i18nKey) {
        super(i18nKey);
        this.fieldName = fieldName;
        this.receivedValue = receivedValue;
        this.i18nKey = i18nKey;
    }
    // getters
}
```

 Subclases intermedias
```java
public abstract class NullValueException extends MissingSemanticValueException {
public NullValueException(String fieldName, String i18nKey) {
super(fieldName, null, i18nKey);
}
}

public abstract class BlankValueException extends MissingSemanticValueException {
public BlankValueException(String fieldName, String receivedValue, String i18nKey) {
super(fieldName, receivedValue, i18nKey);
}
}
```

Excepciones concretas
```java
public class NullAddressFieldException extends NullValueException {
public NullAddressFieldException(String fieldName) {
super(fieldName, "error.person.address.null");
}
}

public class BlankAddressFieldException extends BlankValueException {
public BlankAddressFieldException(String fieldName, String value) {
super(fieldName, value, "error.person.address.blank");
}
}
```

Uso en VOs
```java
if (street == null) throw new NullAddressFieldException("street");
if (street.isBlank()) throw new BlankAddressFieldException("street", street);
```

## Consecuencias y riesgos
- Positivas: facilita auditoría, pruebas automatizadas y generación de catálogo i18n.
- Riesgos:
    - Ambigüedad por nombres duplicados → mitigar documentando paquete completo.
    - Riesgo en reflexión/escaneo → diseñar escáneres por paquete o por base común (ValueObjectValidationException).

## Pasos siguientes
1. Implementar plantilla base y generar excepciones concretas para VOs prioritarios (Address, FullName, PhoneNumber, Dni).
2. Crear catálogo CSV/Markdown con columnas: paquete, clase, campo, i18nKey, severity, mensaje por defecto.
3. Agregar reglas de lint/review para evitar importaciones cruzadas indebidas.
4. Ajustar escáneres/reflection para operar por paquete o por clase base.
5. Actualizar ADR-03 (Catálogo de errores clínicos) con ejemplos concretos del catálogo una vez generados.

## Relación con otros ADR
- [ADR-02 (Arquitectura): Catálogo de errores clínicos por operación](ADR-02-Catálogo%20de%20errores%20clínicos%20por%20operación.md)
- [ADR-03 (Arquitectura): Jerarquía global de excepciones y excepciones para Value Objects de Persona](ADR-03-Jerarquía%20global%20de%20excepciones%20y%20excepciones%20para%20Value%20Objects%20de%20Persona.md)  
  