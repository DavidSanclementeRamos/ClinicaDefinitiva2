

# Value Object: TypeGuardian(TipoResponsable)

## Propósito

TipoResponsable encapsula el rol o vínculo de un Guardian(responsable) con el paciente (ej. madre, padre, abuelo, tutor legal).  
Su objetivo es representar este concepto como una unidad semántica validada, evitando el uso de cadenas planas o enumeraciones rígidas que no permiten extensión ni internacionalización.

Este VO combina la claridad de un catálogo estático (familia nuclear) con la flexibilidad de variantes dinámicas (ej. acudiente institucional), garantizando trazabilidad y legitimidad en el dominio clínico.

## Motivación

El tipo de responsable es un dato crítico en la relación clínica y legal:

- Determina quién puede autorizar procedimientos.
- Impacta en la trazabilidad de consentimientos informados.
- Afecta la comunicación clínica y administrativa.
- Puede variar según contexto cultural, legal o institucional.

Un enum simple no permite extender ni internacionalizar adecuadamente estos roles.  
El VO asegura coherencia semántica, extensibilidad y auditabilidad.

## Estructura

```java
public final class  TypeGuardian {

    private final String code;
    private final String description;

    private TypeGuardian(String code, String description) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El código no puede ser vacío");
        }
        this.code = code.toUpperCase();
        this.description = description;
    }// description

    // Fábrica estática para variantes dinámicas
    public static TypeGuardian of(String code, String description) {
        return new TypeGuardian(code, description);
    }

    // Instancias estáticas (familia nuclear)
    public static final TypeGuardian MAMA     = new TypeGuardian("MAMA", "Madre");
    public static final TypeGuardian PAPA     = new TypeGuardian("PAPA", "Padre");
    public static final TypeGuardian HERMANO  = new TypeGuardian("HERMANO", "Hermano");
    public static final TypeGuardian HERMANA  = new TypeGuardian("HERMANA", "Hermana");
    public static final TypeGuardian ABUELO   = new TypeGuardian("ABUELO", "Abuelo");
    public static final TypeGuardian ABUELA   = new TypeGuardian("ABUELA", "Abuela");
    // … y así con los demás

    // Getters
    public String getCode() { return code; }
    public String getDescription() { return description; }

    // Igualdad semántica
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeGuardian)) return false;
        TypeGuardian that = (TypeGuardian) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return description + " (" + code + ")";
    }


    }


```

## Reglas clínicas encapsuladas

- El codigo es obligatorio, único y no puede ser vacío.
- La igualdad semántica se define por el codigo, no por la descripción.
- Los roles de familia nuclear están predefinidos como instancias estáticas.
- Se permite crear variantes dinámicas mediante of(...) para casos institucionales o legales.
- Preparado para internacionalización: la descripcion puede provenir de catálogos externos.

## Uso en el modelo

- El agregado Guardian utiliza TipoResponsable para identificar su relación con el paciente.
- Se emplea en validaciones de consentimiento informado y trazabilidad clínica.
- Puede ser usado en reportes, contratos y comunicación con familiares o instituciones.

## Ventajas

- Claridad semántica: evita cadenas planas o enums rígidos.
- Extensibilidad: permite agregar nuevos roles sin recompilar.
- Internacionalización: la descripción puede adaptarse a distintos idiomas.
- Trazabilidad: igualdad semántica clara y defendible.
- Exhibitable: muestra la intención del dominio clínico.

## Proyección

- Integración con catálogos externos de roles familiares e institucionales.
- Validación contra normativas legales locales (ej. tutor legal obligatorio en menores).
- Registro dinámico de nuevos roles en un catálogo centralizado.
- Soporte multilenguaje para la descripción.

## Relación con ADR

- ADR-07: Representación de TipoResponsable como Value Object híbrido.
- ADR-02: Implementación estratégica de VO.
- ADR-01: Migración a arquitectura hexagonal.
  