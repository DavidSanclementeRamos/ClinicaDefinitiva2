## ADR-043: Jerarquía de excepciones para valores faltantes en Value Objects
Fecha: 2025-10-03
Estado: Aprobado
Decidido por: David 

## Contexto
Se requiere una convención clara, trazable y reutilizable para las excepciones que representan la ausencia de un valor con significado semántico en Value Objects (VOs) distribuidos por dominios (por ejemplo, person, schedule, clinical). Estas excepciones deben facilitar: (1) manejo por semántica (todos los “missing semantic value”), (2) manejo por contexto/paquete (errores pertenecientes a un dominio) y (3) auditoría/internacionalización con metadatos consistentes.

## Decisión
Adoptar una jerarquía consistente por paquete que combina un nombre semántico unido (MissingSemanticValueException) con subcategorías intermedias para distinguir null y blank. Cada paquete declara su propia MissingSemanticValueException que hereda de la excepción contextual del dominio.
## Estructura general por paquete:
```
ValueObjectValidationException
├── Shared<Domain>Exception
│   └── MissingSemanticValueException
│       ├── NullValueException
│       │   └── Null<Field>NameException
│       ├── BlankValueException
│       │   └── Blank<Field>NameException
│       └── (futuras subcategorías p.ej. InvalidFormatException)
```
## Ejemplo concreto para el paquete domain.person.vo.exception:
```
ValueObjectValidationException
└── SharedPersonException
└── MissingSemanticValueException
├── NullValueException
│   └── NullAddressFieldException
└── BlankValueException
└── BlankAddressFieldException
```
En domain.schedule.vo.exception se mantiene la misma forma pero la MissingSemanticValueException extiende SharedScheduleException.

## Justificación
• 	Consistencia terminológica: el nombre MissingSemanticValueException transmite la misma intención en todos los dominios, facilitando catalogado y documentación.
• 	Contextualización por paquete: al declarar la clase dentro del paquete del dominio y hacerla heredar de la excepción contextual, se preserva la separación de responsabilidades y la trazabilidad del origen.
• 	Distinción técnica y semántica: las subcategorías NullValueException y BlankValueException permiten mensajes y tratamiento operativo distintos cuando se requiera.
• 	Manejo flexible: capas internas pueden capturar MissingSemanticValueException para manejar semántica común; capas superiores o gateways pueden capturar por las excepciones contextuales (SharedPersonException) para políticas específicas de dominio.
• 	Auditoría e I18n: la uniformidad del nombre facilita generar catálogos y claves de internacionalización coherentes.

# Implementación recomendada (plantillas y prácticas)
1. 	Firma mínima de excepciones (metadatos obligatorios):
      • 	campo (fieldName)
      • 	valor recibido (optional)
      • 	contextoId / entityId (optional)
      • 	i18nKey
      • 	severity (p.ej. ERROR, WARNING)
      • 	timestamp (opcional en logging)
2. ##	Plantilla base (en cada paquete de dominio):
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
       
3. 	## Subclases intermedias:

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
4. ##	Excepción concreta:

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

 5. ##	Uso en VOs:
      • 	Validar null antes de usar isBlank.
      • 	Lanzar la excepción específica con fieldName y i18nKey.
      Ejemplo:
```java
if (street == null) throw new NullAddressFieldException("street");
if (street.isBlank()) throw new BlankAddressFieldException("street", street);
```

 6. ## 	Convenciones de paquete:
      • 	Cada dominio declara su MissingSemanticValueException en su paquete domain.<x>.vo.exception.
      • 	Documentar siempre la ruta completa del paquete en catálogos y ADRs.
7. ##	Manejo en capas superiores:
      • 	Validación por semántica dentro del módulo: catch (domain.person.vo.exception.MissingSemanticValueException e) {...}
      • 	Traducción en borde (API Gateway) a DTO de error neutral con código i18n y fieldName.

## Consecuencias y riesgos
• 	Riesgo de ambigüedad por simple-name duplicado: mitigar documentando paquete completo y usando cualificados en puntos de ambigüedad.
• 	Riesgo en reflexión/escaneo: diseñar escáneres por paquete o por base común (ValueObjectValidationException) en lugar de por simple-name.
• 	Beneficio: facilita auditoría, pruebas automatizadas y generación de catálogo i18n.

## pasos siguientes
1. 	Implementar plantilla base y generar excepciones concretas para VOs prioritarios (Address, FullName, PhoneNumber, Dni).
2. 	Crear catálogo CSV/Markdown con columnas: paquete, clase, campo, i18nKey, severity, mensaje por defecto.
3. 	Agregar reglas de lint/review para evitar importaciones cruzadas indebidas.
4. 	Ajustar escáneres/reflection para operar por paquete o por clase base.
5. 	Actualizar ADR-043 con ejemplos concretos del catálogo una vez generados.
